package com.tianji.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tianji.api.client.exam.ExamClient;
import com.tianji.api.dto.course.CatalogueDTO;
import com.tianji.api.dto.course.MediaQuoteDTO;
import com.tianji.api.dto.course.SectionInfoDTO;
import com.tianji.api.dto.exam.QuestionBizDTO;
import com.tianji.common.exceptions.BizIllegalException;
import com.tianji.common.utils.*;
import com.tianji.course.constants.CourseConstants;
import com.tianji.course.constants.CourseErrorInfo;
import com.tianji.course.domain.po.CourseCatalogue;
import com.tianji.course.domain.vo.CataSimpleInfoVO;
import com.tianji.course.domain.vo.CataVO;
import com.tianji.course.mapper.CourseCatalogueMapper;
import com.tianji.course.properties.CourseProperties;
import com.tianji.course.service.ICourseCatalogueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 目录草稿 服务实现类
 * </p>
 *
 * @author wusongsong
 * @since 2022-07-19
 */
@Service
public class CourseCatalogueServiceImpl extends ServiceImpl<CourseCatalogueMapper, CourseCatalogue> implements ICourseCatalogueService {

    @Autowired
    private CourseProperties courseProperties;

    @Autowired
    private ExamClient examClient;

    @Override
    public List<CatalogueDTO> queryCourseCatalogues(Long courseId, Boolean withPractice) {
        //1.课程目录查询条件
        LambdaQueryWrapper<CourseCatalogue> queryWrapper =
                Wrappers.lambdaQuery(CourseCatalogue.class)
                        .eq(CourseCatalogue::getCourseId, courseId);
        if (!withPractice) {
            //1.1课程目录不带练习设置目录查询类型
            queryWrapper.in(CourseCatalogue::getType,
                    Arrays.asList(CourseConstants.CataType.SECTION,
                            CourseConstants.CataType.CHAPTER));
        }
        //1.2根据目录类型和序号排序
        queryWrapper.last(" order by type,c_index");
        //2.查询课程目录列表
        List<CourseCatalogue> courseCatalogues = baseMapper.selectList(queryWrapper);
        if (CollUtils.isEmpty(courseCatalogues)) {
            return null;
        }
        //3.查询课程目录对应题目数量
        Set<Long> ids = courseCatalogues.stream().map(CourseCatalogue::getId).collect(Collectors.toSet());
        List<QuestionBizDTO> questionBizDTOS = examClient.queryQuestionIdsByBizIds(ids);
        //4.转化目录id和题目id、分数对应关系
        Map<Long, Long> cataIdAndNumMap =
                CollUtils.isEmpty(questionBizDTOS)
                        ? new HashMap<>() :
                        questionBizDTOS
                                .stream()
                                .collect(Collectors.groupingBy(QuestionBizDTO::getBizId, Collectors.counting()));
        // 5.组织树结构并返回
        return TreeDataUtils.parseToTree(courseCatalogues, CatalogueDTO.class, (courseCatalogue, cataVO)->{
            cataVO.setMediaName(courseCatalogue.getVideoName());
            cataVO.setIndex(courseCatalogue.getCIndex());
            cataVO.setSubjectNum(cataIdAndNumMap.getOrDefault(courseCatalogue.getId(), 0L).intValue());
        }, new CourseCatalogDataWrapper());
    }

    @Override
    public List<MediaQuoteDTO> countMediaUserInfo(List<Long> mediaIds) {
        //1.判断媒资id列表为空判断
        if (CollUtils.isEmpty(mediaIds)) {
            return CollUtils.emptyList();
        }
        //2.课程目录查询条件
        LambdaQueryWrapper<CourseCatalogue> queryWrapper =
                Wrappers.lambdaQuery(CourseCatalogue.class)
                .in(CourseCatalogue::getMediaId, mediaIds);
        //2.1查询课程目录列表
        List<CourseCatalogue> courseCatalogues = baseMapper.selectList(queryWrapper);
        //3.未查询到媒资对应的课程目录
        if (CollUtils.isEmpty(courseCatalogues)) {
            //3.1媒资都未被引用，都给一个引用次数0
            return mediaIds.stream()
                    .map(mediaId -> new MediaQuoteDTO(mediaId, 0))
                    .collect(Collectors.toList());
        }
        //4.分组统计媒资被引用次数
        Map<Long, Long> mediaAndCount =
                courseCatalogues.stream()
                .collect(Collectors.groupingBy(
                        CourseCatalogue::getMediaId, Collectors.counting()));
        //5.组装数据，设置
        return mediaIds.stream().map(
                mediaId -> new MediaQuoteDTO(mediaId,
                        NumberUtils.null2Zero(mediaAndCount.get(mediaId))
                                .intValue())
        ).collect(Collectors.toList());
    }

    @Override
    public SectionInfoDTO getSimpleSectionInfo(Long sectionId) {
        //1.小节id判空
        if (sectionId == null) {
            throw new BizIllegalException(CourseErrorInfo.Msg.CATEGORY_QUERY_ID_NULL);
        }
        //2.获取小节对应的目录信息
        CourseCatalogue courseCatalogue = baseMapper.selectById(sectionId);
        if (courseCatalogue == null) {
            return new SectionInfoDTO();
        }
        //3.判断目录类型是否为小节
        if (courseCatalogue.getType() != CourseConstants.CataType.SECTION) {
            return new SectionInfoDTO();
        }
        //4.组装数据
        SectionInfoDTO sectionInfoDTO = BeanUtils.toBean(courseCatalogue, SectionInfoDTO.class);
        //5.设置免费试看时长
        sectionInfoDTO.setFreeDuration(courseCatalogue.getTrailer() == 1 ?
                courseProperties.getMedia().getTrailerDuration() : 0);
        return sectionInfoDTO;
    }

    @Override
    public List<CataSimpleInfoVO> getCatasIndexList(Long courseId) {
        //1.课程目录（不含练习）查询条件
        LambdaQueryWrapper<CourseCatalogue> queryWrapper =
                Wrappers.lambdaQuery(CourseCatalogue.class)
                        .eq(CourseCatalogue::getCourseId, courseId)
                        .in(CourseCatalogue::getType, Arrays.asList(
                                CourseConstants.CataType.CHAPTER,
                                CourseConstants.CataType.SECTION
                        ));
        //1.1查询课程目录
        List<CourseCatalogue> courseCatalogues = baseMapper.selectList(queryWrapper);
        if (CollUtils.isEmpty(courseCatalogues)) {
            return new ArrayList<>();
        }
        //2.章id与章序号映射关系
        Map<Long, Integer> chapterMap =
                courseCatalogues
                        .stream()
                        .filter(courseCatalogue -> courseCatalogue.getType() == CourseConstants.CataType.CHAPTER)
                        .collect(Collectors.toMap(CourseCatalogue::getId, CourseCatalogue::getCIndex));
        //3.遍历课程目录，组装数据
        return courseCatalogues.stream()
                .filter(courseCatalogue -> courseCatalogue.getType() != CourseConstants.CataType.CHAPTER)

                .map(courseCatalogue -> {
                    //3.1组装目录序号
                    String index = StringUtils.format("{}-{}",
                            chapterMap.get(courseCatalogue.getParentCatalogueId()),
                            courseCatalogue.getCIndex());
                    //3.2组装目录信息，目录id，目录名称，目录序号
                    return new CataSimpleInfoVO(courseCatalogue.getId(),
                            courseCatalogue.getName(), index, courseCatalogue.getCIndex(), null);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<CataSimpleInfoVO> getManyCataSimpleInfo(List<Long> ids) {
        // 1.判空
        if(CollUtils.isEmpty(ids)){
            return CollUtils.emptyList();
        }
        //2.查询条件
        LambdaQueryWrapper<CourseCatalogue> queryWrapper =
                Wrappers.lambdaQuery(CourseCatalogue.class)
                .in(CourseCatalogue::getId, ids);
        //3.查询数据
        List<CourseCatalogue> courseCatalogues = baseMapper.selectList(queryWrapper);
        //4.数据转化
        return BeanUtils.copyList(courseCatalogues, CataSimpleInfoVO.class);
    }

    @Override
    public CataSimpleInfoVO querySectionInfoById(Long id) {
        // 1.查询数据
        CourseCatalogue currentCourseCatalogue = baseMapper.selectById(id);
        // 2.判断是否是小节
        if(currentCourseCatalogue != null
                && currentCourseCatalogue.getType() == CourseConstants.CataType.SECTION) {
            // 2.1.转换数据
            CataSimpleInfoVO cataSimpleInfoVO = BeanUtils.toBean(currentCourseCatalogue, CataSimpleInfoVO.class);
            // 3.查询章信息
            CourseCatalogue courseCatalogue = baseMapper.selectById(currentCourseCatalogue.getParentCatalogueId());
            // 3.1.章id
            cataSimpleInfoVO.setChapterIndex(courseCatalogue.getCIndex());
            return cataSimpleInfoVO;
        }
        // 4.返回空数据
        return new CataSimpleInfoVO();
    }

    @Override
    public List<CataVO> queryCourseCataloguesVO(Long courseId, Boolean withPractice) {
        //1.课程目录查询条件
        LambdaQueryWrapper<CourseCatalogue> queryWrapper =
                Wrappers.lambdaQuery(CourseCatalogue.class)
                        .eq(CourseCatalogue::getCourseId, courseId);
        if (!withPractice) {
            //1.1课程目录不带练习设置目录查询类型
            queryWrapper.in(CourseCatalogue::getType,
                    Arrays.asList(CourseConstants.CataType.SECTION,
                            CourseConstants.CataType.CHAPTER));
        }
        //1.2根据目录类型和序号排序
        queryWrapper.last(" order by type,c_index");
        //2.查询课程目录列表
        List<CourseCatalogue> courseCatalogues = baseMapper.selectList(queryWrapper);
        if (CollUtils.isEmpty(courseCatalogues)) {
            return null;
        }

        //3.查询课程目录id、题目id及分数列表
        Set<Long> ids = courseCatalogues.stream().map(CourseCatalogue::getId).collect(Collectors.toSet());
        List<QuestionBizDTO> questionBizDTOS = examClient.queryQuestionIdsByBizIds(ids);
        //4.转化目录id和题目id、分数对应关系
        Map<Long, Long> cataIdAndNumMap =
                CollUtils.isEmpty(questionBizDTOS)
                        ? new HashMap<>() :
                        questionBizDTOS
                                .stream()
                                .collect(Collectors.groupingBy(QuestionBizDTO::getBizId, Collectors.counting()));
        //5.转化录id和题目id、总分数关系
        Map<Long, Integer> cataIdAndTotalScoreMap = examClient.queryQuestionScoresByBizIds(ids);
        //6.数据目录结构转化
        List<CataVO> cataVOS =
                TreeDataUtils.parseToTree(courseCatalogues, CataVO.class,
                        (courseCatalogue, cataVO) -> {
                            //6.1设置媒资名称
                            cataVO.setMediaName(courseCatalogue.getVideoName());
                            //6.2设置目录索引
                            cataVO.setIndex(courseCatalogue.getCIndex());
                            //6.3设置题目数量
                            cataVO.setSubjectNum(NumberUtils.null2Zero(
                                    cataIdAndNumMap.get(courseCatalogue.getId()))
                                    .intValue()); //练习总数量
                            //6.4设置题目总分数
                            cataVO.setTotalScore(NumberUtils.null2Zero(
                                    cataIdAndTotalScoreMap.get(courseCatalogue.getId()))); //练习总分数
                        }, new CourseCatalogDataWrapper2());

        return cataVOS;
    }

    //课程目录树形转化模型
    private static class CourseCatalogDataWrapper implements TreeDataUtils.DataProcessor<CatalogueDTO, CourseCatalogue> {


        @Override
        public Object getParentKey(CourseCatalogue courseCatalogue) {
            return courseCatalogue.getParentCatalogueId();
        }

        @Override
        public Object getKey(CourseCatalogue courseCatalogue) {
            return courseCatalogue.getId();
        }

        @Override
        public Object getRootKey() {
            return 0L;
        }

        @Override
        public List<CatalogueDTO> getChild(CatalogueDTO catalogueDTO) {
            return catalogueDTO.getSections();
        }

        @Override
        public void setChild(CatalogueDTO parent, List<CatalogueDTO> child) {
            parent.setSections(child);
        }
    }

    //课程目录树形转化模型
    private static class CourseCatalogDataWrapper2 implements TreeDataUtils.DataProcessor<CataVO, CourseCatalogue> {


        @Override
        public Object getParentKey(CourseCatalogue courseCatalogue) {
            return courseCatalogue.getParentCatalogueId();
        }

        @Override
        public Object getKey(CourseCatalogue courseCatalogue) {
            return courseCatalogue.getId();
        }

        @Override
        public Object getRootKey() {
            return 0L;
        }

        @Override
        public List<CataVO> getChild(CataVO catalogueDTO) {
            return catalogueDTO.getSections();
        }

        @Override
        public void setChild(CataVO parent, List<CataVO> child) {
            parent.setSections(child);
        }
    }
}
