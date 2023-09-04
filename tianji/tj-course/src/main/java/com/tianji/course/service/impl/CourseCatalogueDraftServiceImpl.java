package com.tianji.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tianji.api.client.exam.ExamClient;
import com.tianji.api.dto.exam.QuestionBizDTO;
import com.tianji.api.dto.exam.QuestionDTO;
import com.tianji.common.constants.ErrorInfo;
import com.tianji.common.exceptions.BizIllegalException;
import com.tianji.common.exceptions.DbException;
import com.tianji.common.utils.*;
import com.tianji.common.validate.Checker;
import com.tianji.course.constants.CourseConstants;
import com.tianji.course.constants.CourseErrorInfo;
import com.tianji.course.constants.CourseStatus;
import com.tianji.course.domain.dto.CataSaveDTO;
import com.tianji.course.domain.dto.CataSubjectDTO;
import com.tianji.course.domain.dto.CourseMediaDTO;
import com.tianji.course.domain.po.*;
import com.tianji.course.domain.vo.CataSimpleSubjectVO;
import com.tianji.course.domain.vo.CataVO;
import com.tianji.course.mapper.CourseCataSubjectDraftMapper;
import com.tianji.course.mapper.CourseCataSubjectMapper;
import com.tianji.course.mapper.CourseCatalogueDraftMapper;
import com.tianji.course.mapper.CourseCatalogueMapper;
import com.tianji.course.service.ICourseCataSubjectDraftService;
import com.tianji.course.service.ICourseCatalogueDraftService;
import com.tianji.course.service.ICourseCatalogueService;
import com.tianji.course.service.ICourseDraftService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
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
@Slf4j
public class CourseCatalogueDraftServiceImpl extends ServiceImpl<CourseCatalogueDraftMapper, CourseCatalogueDraft> implements ICourseCatalogueDraftService {

    @Autowired
    private CourseCatalogueMapper courseCatalogueMapper;

    @Autowired
    private ICourseCatalogueService courseCatalogueService;

    @Autowired
    private ICourseDraftService courseDraftService;

    @Autowired
    private CourseCataSubjectDraftMapper courseCataSubjectDraftMapper;

    @Autowired
    private CourseCataSubjectMapper courseCataSubjectMapper;

    @Autowired
    private CourseCatalogueDraftMapper courseCatalogueDraftMapper;

    @Autowired
    private ExamClient examClient;

    @Autowired
    private ICourseCataSubjectDraftService courseCataSubjectDraftService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {DbException.class, Exception.class})
    public void save(Long courseId, List<CataSaveDTO> cataSaveDTOS, Integer step) {
        //1.根据章的序号按照升序重新排序
        cataSaveDTOS = cataSaveDTOS
                .stream()
                .sorted(Comparator.comparing(CataSaveDTO::getIndex))
                .collect(Collectors.toList());

        //2.校验章的序号
        if (cataSaveDTOS.size() != cataSaveDTOS
                .stream()
                .map(CataSaveDTO::getIndex)
                .distinct()
                .count()) {
            throw new BizIllegalException(CourseErrorInfo.Msg.COURSE_CATAS_SAVE_INEDX);
        }
        if (cataSaveDTOS.size() < cataSaveDTOS.get(cataSaveDTOS.size() - 1).getIndex()) {
            throw new BizIllegalException(CourseErrorInfo.Msg.COURSE_CATAS_SAVE_INEDX_JUMP);
        }

        //2.已经上架的目录
        LambdaQueryWrapper<CourseCatalogue> queryWrapper =
                Wrappers.lambdaQuery(CourseCatalogue.class)
                        .eq(CourseCatalogue::getCourseId, courseId);
        List<CourseCatalogue> courseCatalogues = courseCatalogueMapper.selectList(queryWrapper);

        //2.1.校验以上架目录是否更新
        checkIndex(cataSaveDTOS, courseCatalogues);
        //3.组装数据保存到数据库
        List<CourseCatalogueDraft> courseCatalogueDrafts =
                packageCatalogue(courseId, cataSaveDTOS, courseCatalogues);
        //4.删除原有目录信息
        if (step == CourseConstants.CourseStep.CATALOGUE) {
            //4.1删除小节和章数据
            courseCatalogueDraftMapper.deleteByCourseId(courseId,
                    Arrays.asList(
                            CourseConstants.CataType.CHAPTER,
                            CourseConstants.CataType.SECTION));
        } else if (step == CourseConstants.CourseStep.SUBJECT) {
            //4.2保存题目时需要删除所有目录
            courseCatalogueDraftMapper.deleteByCourseId(courseId,
                    Arrays.asList(
                            CourseConstants.CataType.CHAPTER,
                            CourseConstants.CataType.SECTION,
                            CourseConstants.CataType.PRATICE));
        } else {
            throw new BizIllegalException(ErrorInfo.Msg.OPERATE_FAILED);
        }
        //5.目录重新插入草稿
        this.saveOrUpdateBatch(courseCatalogueDrafts);

        //6.修改课程编辑进度
        courseDraftService.updateStep(courseId, CourseConstants.CourseStep.CATALOGUE);

        //7.删除已删除章节题目
        courseCataSubjectDraftService.deleteNotInCataIdList(courseId);
    }

    @Override
    public List<CataVO> queryCourseCatalogues(Long courseId, Boolean see, Boolean withPractice) {
        if (see) {
            //1.1查询正式数据目录
            List<CataVO> cataVOS = courseCatalogueService.queryCourseCataloguesVO(courseId, withPractice);
            if (CollUtils.isNotEmpty(cataVOS)) {
                return cataVOS;
            }
            //1.2查看草稿目录
            cataVOS = queryCourseCatalogues(courseId, withPractice);
            return CollUtils.isEmpty(cataVOS) ? new ArrayList<>() : cataVOS;

        } else {
            //2.1查看草稿目录
            List<CataVO> cataVOS = queryCourseCatalogues(courseId, withPractice);
            return CollUtils.isEmpty(cataVOS) ? new ArrayList<>() : cataVOS;
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {DbException.class, Exception.class})
    public void saveMediaInfo(Long courseId, List<CourseMediaDTO> courseMediaDTOS) {
        //1.校验保存视频的小节id是否属于当前课程中的小节，
        List<Long> cataIds =
                courseMediaDTOS.stream()
                        .map(CourseMediaDTO::getCataId)
                        .collect(Collectors.toList());
        //2.每个小节都有上传媒资
        checkSectionIds(cataIds, courseId);

        //3.获取课程草稿信息
        CourseDraft courseDraft = courseDraftService.getById(courseId);
        //3.1判断新增课程是否按照顺序上传媒资
        if (courseDraft == null ||
                courseDraft.getStep() < CourseConstants.CourseStep.CATALOGUE) {
            throw new BizIllegalException(CourseErrorInfo.Msg.COURSE_MEDIA_SAVE_NO_EXECUTE);
        }

        //4.媒资设置到小节信息中
        List<CourseCatalogueDraft> catalogueDrafts =
                BeanUtils.copyList(courseMediaDTOS, CourseCatalogueDraft.class,
                        (dto, courseCatalogueDraft) ->
                                courseCatalogueDraft.setId(dto.getCataId()));
        //4.1.更新小节媒资信息
        this.updateBatchById(catalogueDrafts);
        //4.2.更新课程填写步骤
        courseDraftService.updateStep(courseId, CourseConstants.CourseStep.MEDIA);
        //5.统计每个章节媒资播放总时长
        List<CourseCatalogueDraft> courseCatalogueDrafts = calculateCatalogMediaDuration(courseId);
        //5.1批量更新每个大章的课时总数量
        this.updateBatchById(courseCatalogueDrafts, 500);


    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {DbException.class, Exception.class})
    public void saveSuject(Long courseId, List<CataSubjectDTO> cataSubjectDTOS) {
        //1.数据校验
        //1.1.转化目录id列表
        List<Long> cataIds = cataSubjectDTOS
                .stream()
                .map(CataSubjectDTO::getCataId)
                .collect(Collectors.toList());
        checkPracticeIds(cataIds, courseId);
        List<CourseCataSubjectDraft> cataSubjectDrafts = new ArrayList<>();

        //2.查询课程草稿
        CourseDraft courseDraft = courseDraftService.getById(courseId);
        //2.1.判断当前是否可以上传题目
        if (courseDraft == null || courseDraft.getStep() < CourseConstants.CourseStep.MEDIA) {
            throw new BizIllegalException(CourseErrorInfo.Msg.COURSE_MEDIA_SAVE_NO_EXECUTE);
        }

        //3.组装题目目录关系
        for (CataSubjectDTO cataSubjectDTO : cataSubjectDTOS) {
            for (Long subjectId : cataSubjectDTO.getSubjectIds()) {
                CourseCataSubjectDraft courseCataSubjectDraft = new CourseCataSubjectDraft();
                //3.1.课程id
                courseCataSubjectDraft.setCourseId(courseId);
                //3.2.题目id
                courseCataSubjectDraft.setSubjectId(subjectId);
                //3.3.课程目录
                courseCataSubjectDraft.setCataId(cataSubjectDTO.getCataId());
                if (courseCataSubjectDraft.getId() == null) {
                    courseCataSubjectDraft.setId(IdWorker.getId());
                }
                //3.4.课程题目关系添加到
                cataSubjectDrafts.add(courseCataSubjectDraft);
            }
        }
        //4.删除练习和题目对应的关系
        courseCataSubjectDraftMapper.deleteByCourseId(courseId);
        //5.批量插入练习和题目之间的关系
        if (!cataSubjectDrafts.isEmpty()) {
            courseCataSubjectDraftMapper.batchInsert(cataSubjectDrafts);
        }
        //6.修改课程填写进度
        courseDraftService.updateStep(courseId, CourseConstants.CourseStep.SUBJECT);
    }

    @Override
    public List<CataSimpleSubjectVO> getSuject(Long courseId) {

        //1.查询课程目录和题目关系
        List<CourseCataSubjectDraft> cataSubjectDrafts = courseCataSubjectDraftMapper.getByCourseId(courseId);
        if (CollUtils.isEmpty(cataSubjectDrafts)) {
            return new ArrayList<>();
        }
        List<Long> subjectIdList = cataSubjectDrafts.stream().map(CourseCataSubjectDraft::getSubjectId).collect(Collectors.toList());
        List<QuestionDTO> subjects = examClient.queryQuestionByIds(subjectIdList);
        Map<Long, String> subjectIdAndNameMap = subjects.stream()
                .collect(Collectors.toMap(QuestionDTO::getId, QuestionDTO::getName));

        //4.组装数据
        return cataSubjectDrafts.stream()
                //4.1.分组
                .collect(Collectors.groupingBy(CourseCataSubjectDraft::getCataId))
                .entrySet().stream().map(entry -> {
                    //4.2.小节或测试对应的题目列表
                    List<CataSimpleSubjectVO.SubjectInfo> subjectInfos = new ArrayList<>();
                    for (CourseCataSubjectDraft cataSubjectDraft : entry.getValue()) {
                        //4.3.将题目id和题目名称加入到小节或测试的题目列表中
                        subjectInfos.add(new CataSimpleSubjectVO.SubjectInfo(
                                cataSubjectDraft.getSubjectId(),
                                subjectIdAndNameMap.get(cataSubjectDraft.getSubjectId())));
                    }
                    //4.4.组装小节或测试对应题目列表model
                    return new CataSimpleSubjectVO(entry.getKey(), subjectInfos);
                }).collect(Collectors.toList());
    }

    @Override
    public void checkCataInfoImplated(Long courseId) {
        //查询所有目录
        LambdaQueryWrapper<CourseCatalogueDraft> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CourseCatalogueDraft::getCourseId, courseId);
        List<CourseCatalogueDraft> courseCatalogueDrafts = baseMapper.selectList(queryWrapper);

        List<CourseCataSubjectDraft> courseCataSubjectDrafts = courseCataSubjectDraftMapper.getByCourseId(courseId);

        Map<Long, Long> subjectNumMap = CollUtils.isEmpty(courseCataSubjectDrafts) ? new HashMap<>() :
                courseCataSubjectDrafts.stream().collect(
                        Collectors.groupingBy(CourseCataSubjectDraft::getCataId,
                                Collectors.counting()));

        //校验练习或小节有没有上传题目和媒资
        CollUtils.check(courseCatalogueDrafts, new Checker<CourseCatalogueDraft>() {
            @Override
            public void check(CourseCatalogueDraft courseCatalogueDraft) {
                if (courseCatalogueDraft.getType() == CourseConstants.CataType.SECTION
                        && StringUtils.isEmpty(courseCatalogueDraft.getVideoName())) { //小节未上传视频
                    throw new BizIllegalException(
                            StringUtils.format(CourseErrorInfo.Msg.COURSE_UP_SHELF_SECTION_WITHOUT_MEDIA, courseCatalogueDraft.getName()));
                } else if (courseCatalogueDraft.getType() == CourseConstants.CataType.PRATICE
                        && NumberUtils.null2Zero(subjectNumMap.get(courseCatalogueDraft.getId())) <= 0) { //练习未添加题目
                    throw new BizIllegalException(
                            StringUtils.format(CourseErrorInfo.Msg.COURSE_UP_SHELF_PRACTICE_WITHOUT_SUBJECT,
                                    courseCatalogueDraft.getName())
                    );
                }
            }
        });
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {DbException.class, Exception.class})
    public void copySubjectToShelf(Long courseId, Boolean isFirstShelf) {
        //1.从草稿中查出题目信息
        List<CourseCataSubjectDraft> courseCataSubjectDrafts = courseCataSubjectDraftMapper.getByCourseId(courseId);
        List<QuestionBizDTO> subjects = courseCataSubjectDrafts.stream()
                .map(s -> QuestionBizDTO.of(s.getCataId(), s.getSubjectId())).collect(Collectors.toList());
        //2.删除练习和题目之间的关系
        if (CollUtils.isEmpty(courseCataSubjectDrafts)) {
            // 草稿中没有题目，直接结束
            return;
        }
        //3.将新的练习和题目之间的关系上架
        examClient.saveQuestionBizInfoBatch(subjects);
        //4.删除草稿
        int result = courseCataSubjectDraftMapper.deleteByCourseId(courseId);
        if (result != courseCataSubjectDrafts.size()) {
            throw new DbException(ErrorInfo.Msg.DB_UPDATE_EXCEPTION);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {DbException.class, Exception.class})
    public void copyToShelf(Long courseId, Boolean isFirstShelf) {
        //1.从草稿中查出目录信息
        List<CourseCatalogueDraft> courseCatalogueDrafts = baseMapper.getByCourseId(courseId);
        List<CourseCatalogue> courseCatalogues = BeanUtils.copyList(courseCatalogueDrafts, CourseCatalogue.class);
        //2.保存数据上架
        boolean result = courseCatalogueService.saveOrUpdateBatch(courseCatalogues);
        if (!result) {
            throw new DbException(ErrorInfo.Msg.DB_UPDATE_EXCEPTION);
        }
        //3.删除草稿
        int deleteResult = courseCatalogueDraftMapper.deleteByCourseId(courseId, Arrays.asList(
                CourseConstants.CataType.CHAPTER,
                CourseConstants.CataType.SECTION,
                CourseConstants.CataType.PRATICE
        ));
        if (deleteResult != courseCatalogueDrafts.size()) {
            throw new DbException(ErrorInfo.Msg.DB_UPDATE_EXCEPTION);
        }
    }

    @Override
    public Map<Long, Integer> calculateMediaDuration(Long courseId) {
        LambdaQueryWrapper<CourseCatalogueDraft> queryWrapper =
                Wrappers.lambdaQuery(CourseCatalogueDraft.class)
                        .eq(CourseCatalogueDraft::getCourseId, courseId)
                        .eq(CourseCatalogueDraft::getType, CourseConstants.CataType.SECTION);
        List<CourseCatalogueDraft> list = list(queryWrapper);
        if (CollUtils.isEmpty(list)) {
            return new HashMap<>();
        }
        return list.stream()
                .collect(Collectors.groupingBy(
                        CourseCatalogueDraft::getParentCatalogueId,
                        Collectors.summingInt(CourseCatalogueDraft::getMediaDuration)));
    }

    @Override
    public Integer totalSectionNums(Long courseId) {
        LambdaQueryWrapper<CourseCatalogueDraft> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CourseCatalogueDraft::getCourseId, courseId)
                .in(CourseCatalogueDraft::getType,
                        Arrays.asList(CourseConstants.CataType.SECTION, CourseConstants.CataType.PRATICE));
        return count(queryWrapper);
    }

    @Override
    public List<Long> queryCataIdsOfCourse(Long courseId, List<Integer> types) {
        //1.查询条件
        LambdaQueryWrapper<CourseCatalogueDraft> queryWrapper =
                Wrappers.lambdaQuery(CourseCatalogueDraft.class)
                        .eq(CourseCatalogueDraft::getCourseId, courseId)
                        .in(CourseCatalogueDraft::getType, types);
        //2.查询数据
        List<CourseCatalogueDraft> courseCatalogueDrafts = baseMapper.selectList(queryWrapper);
        //3.返回数据
        return CollUtils.isEmpty(courseCatalogueDrafts)
                ? new ArrayList<>()
                : courseCatalogueDrafts
                .stream()
                .map(CourseCatalogueDraft::getId)
                .collect(Collectors.toList());
    }


    /**
     * 校验已经上架课程的章节是否被调动或删除
     *
     * @param courseCatalogues 已上架的
     * @param cataSaveDTOS     前端传来要保存的目录列表
     */
    private void checkIndex(List<CataSaveDTO> cataSaveDTOS, List<CourseCatalogue> courseCatalogues) {

        //校验章序号是否有重复的
        Map<Integer, CataSaveDTO> collect = cataSaveDTOS.stream().collect(Collectors.toMap(CataSaveDTO::getIndex, p -> p));
        if (collect.size() < cataSaveDTOS.size()) { //有重复的章序号
            throw new BizIllegalException(CourseErrorInfo.Msg.COURSE_CATAS_SAVE_CHAPTER_INDEX_REPEAT);
        }
        //cataSaveDTOS 是按升序排的 最大的章序号大于章数量，说明章列表cataSaveDTOS的序号有间断设置
        if (cataSaveDTOS.get(cataSaveDTOS.size() - 1).getIndex() > cataSaveDTOS.size()) {
            throw new BizIllegalException(CourseErrorInfo.Msg.COURSE_CATAS_SAVE_CHAPTER_INDEX_INTERRUPTED);
        }

        if (CollUtils.isEmpty(courseCatalogues)) {
            return;
        }
        final Map<Long, Integer> saveIndexMap = new HashMap<>();
        for (CataSaveDTO cataSaveDTO : cataSaveDTOS) {//章的序号
            if (cataSaveDTO.getId() != null) {
                saveIndexMap.put(cataSaveDTO.getId(), cataSaveDTO.getIndex());
            }

            //小节和练习的序号
            if (CollUtils.isEmpty(cataSaveDTO.getSections())) { //章没有对应的小节
                throw new BizIllegalException(CourseErrorInfo.Msg.COURSE_CATAS_SAVE_CHAPTER_WITHOUT_SECTION);
            }
            AtomicInteger count = new AtomicInteger(1);
            //小节需要排序，练习不需要排序
            cataSaveDTO.getSections().stream().filter(section -> section.getType() == CourseConstants.CataType.SECTION)
                    .forEach(section -> {
                        if (section.getId() == null) {
                            //序号从1开始
                            saveIndexMap.put(section.getId(), count.incrementAndGet());
                        }
                    });
        }

        for (CourseCatalogue courseCatalogue : courseCatalogues) {
            if (courseCatalogue.getType() != CourseConstants.CataType.CHAPTER) {
                continue;
            }
            Integer index = saveIndexMap.get(courseCatalogue.getId());
            if (index == null) {
                throw new BizIllegalException(StringUtils.format(CourseErrorInfo.Msg.COURSE_CATAS_SAVE_CHAPTER_NAME_DELETED, courseCatalogue.getName()));
            }
            //章排序
            if (!index.equals(courseCatalogue.getCIndex())) {
                throw new BizIllegalException(StringUtils.format(ErrorInfo.Msg.OPERATE_FAILED, courseCatalogue.getName()));
            }
        }
    }

    /**
     * 组装数据,数据优先级，本次保存数据>草稿中的数据>已上架数据
     * 通过map来实现数据的覆盖，map key的目录id，value是目录信息
     * 1.先将已上架的数据放入map
     * 2.将草稿中的数据放入map，放入的过程中草稿中和已上架都存在，草稿中的数据会自动覆盖掉已上架数据
     * 3.遍历本次保存数据，
     * 3.1 如果目录信息没有id，生成一个
     * 3.2 在map中获取目录信息，没有找到生成一个目录草稿类
     * 3.3 向目录信息中添加要保存的数据 序号、名称、小节id、目录id
     *
     * @param courseId     课程id
     * @param cataSaveDTOS 课程目录列表
     * @return 数据库中存储的课程目录列表
     */
    private List<CourseCatalogueDraft> packageCatalogue(Long courseId, List<CataSaveDTO> cataSaveDTOS, List<CourseCatalogue> shelfCourseCatalogues) {
        final Map<Long, CourseCatalogueDraft> savedMap = new HashMap<>();
        if (CollUtils.isNotEmpty(shelfCourseCatalogues)) {
            //将已经上架的目录copy下来
            shelfCourseCatalogues.stream().forEach(courseCatalogue ->
                    savedMap.put(courseCatalogue.getId(), BeanUtils.toBean(courseCatalogue, CourseCatalogueDraft.class)));
        }
        //获取已经保存的草稿
        LambdaQueryWrapper<CourseCatalogueDraft> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CourseCatalogueDraft::getCourseId, courseId);
        List<CourseCatalogueDraft> savedCourseCataloguesDraft = list(queryWrapper);
        if (CollUtils.isNotEmpty(savedCourseCataloguesDraft)) {
            //存在草稿覆盖已经上架的课程
            savedCourseCataloguesDraft.stream().forEach(courseCatalogueDraft ->
                    savedMap.put(courseCatalogueDraft.getId(), courseCatalogueDraft));
        }
        //需要保存的草稿
        List<CourseCatalogueDraft> courseCatalogueDrafts = new ArrayList<>();
        for (CataSaveDTO cataSaveDTO : cataSaveDTOS) {
            Long chapterId = cataSaveDTO.getId() == null ? IdWorker.getId() : cataSaveDTO.getId();
            //章目录
            CourseCatalogueDraft courseCatalogueDraft = savedMap.get(chapterId);
            if (courseCatalogueDraft == null) { //未保存过
                courseCatalogueDraft = new CourseCatalogueDraft();
                courseCatalogueDraft.setId(chapterId);
            }

            //设置目录添加或修改时的基本信息
            courseCatalogueDraft.setCataBaseInfo(cataSaveDTO.getIndex(), cataSaveDTO.getName(), CourseConstants.CataType.CHAPTER,
                    0L, courseId);
            courseCatalogueDrafts.add(courseCatalogueDraft);
            //小节练习目录，小节有需要，练习没有需要
            //序号
            AtomicInteger indexCount = new AtomicInteger(0);
            cataSaveDTO.getSections().stream().forEach(section -> {
                Long sectionId = section.getId() == null ? IdWorker.getId() : section.getId();
                //小节练习目录
                CourseCatalogueDraft courseCatalogueDraftSection = savedMap.get(sectionId);
                if (courseCatalogueDraftSection == null) { //未保存过
                    courseCatalogueDraftSection = new CourseCatalogueDraft();
                    courseCatalogueDraftSection.setId(sectionId);
                }
                Integer index = section.getType() == CourseConstants.CataType.SECTION ?
                        indexCount.incrementAndGet() : null;

                //设置目录添加或修改时的基本信息
                courseCatalogueDraftSection.setCataBaseInfo(index, section.getName(), section.getType(),
                        chapterId, courseId);
                courseCatalogueDrafts.add(courseCatalogueDraftSection);
            });
        }
        return courseCatalogueDrafts;
    }

    private List<CataVO> queryCourseCatalogues(Long courseId, Boolean withPractice) {
        LambdaQueryWrapper<CourseCatalogueDraft> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CourseCatalogueDraft::getCourseId, courseId);
        if (!withPractice) {
            queryWrapper.in(CourseCatalogueDraft::getType,
                    Arrays.asList(CourseConstants.CataType.CHAPTER, CourseConstants.CataType.SECTION));
        }
        //根据类型和排序，按照
        queryWrapper.last(" order by type,c_index");
        List<CourseCatalogueDraft> courseCatalogueDrafts = baseMapper.selectList(queryWrapper);
        if (CollUtils.isEmpty(courseCatalogueDrafts)) {
            return null;
        }
        CourseDraft courseDraft = courseDraftService.getById(courseId);

        // 最大上架数,待上架设置空map，已上架需要排序并去小节序号（同一个章中）中最大小节
        Map<Long, CourseCatalogueDraft> chapterIdAndMaxSectionMap =
                (courseDraft.getStatus() == CourseStatus.NO_UP_SHELF.getStatus())
                        ? new HashMap<>() :
                        courseCatalogueDrafts.parallelStream()
                                .filter(ccd -> ccd.getType() == CourseConstants.CataType.SECTION && !ccd.getCanUpdate())
                                .collect(Collectors.groupingBy(CourseCatalogueDraft::getParentCatalogueId,
                                        Collectors.collectingAndThen(
                                                Collectors.reducing(
                                                        (c1, c2) -> c2.getCIndex().compareTo(c1.getCIndex()) > 0 ? c2 : c1),
                                                Optional::get)));
        int maxChapterIndex = (courseDraft.getStatus() == CourseStatus.NO_UP_SHELF.getStatus())
                ? 0
                : courseCatalogueDrafts.stream()
                .filter(ccd -> ccd.getType() == CourseConstants.CataType.CHAPTER && !ccd.getCanUpdate())
                .map(CourseCatalogueDraft::getCIndex)
                .max(Integer::compare).get();


        // 4.查询课程对应的小节和题目信息
        List<CourseCataSubjectDraft> subjects = courseCataSubjectDraftMapper.getByCourseId(courseId);
        // 4.1.统计题目数量
        Map<Long, Long> cataIdAndNumMap = CollUtils.isEmpty(subjects) ? new HashMap<>() :
                subjects.stream().collect(Collectors.groupingBy(CourseCataSubjectDraft::getCataId, Collectors.counting()));
        // 4.2.查询分数
        Map<Long, Integer> cataIdAndTotalScoreMap = new HashMap<>(cataIdAndNumMap.size());
        if (CollUtils.isNotEmpty(subjects)) {
            Set<Long> questionIds = subjects.stream().map(CourseCataSubjectDraft::getSubjectId).collect(Collectors.toSet());
            Map<Long, Integer> scoreMap = examClient.queryQuestionScores(questionIds);
            cataIdAndTotalScoreMap.putAll(
                    subjects.stream().collect(Collectors.groupingBy(
                            CourseCataSubjectDraft::getCataId,
                            Collectors.summingInt(d -> scoreMap.get(d.getSubjectId()))
                    )));
        }
        return TreeDataUtils.parseToTree(courseCatalogueDrafts, CataVO.class, (catalogueDraft, vo) -> {
            int maxIndexOnShelf = 0;
            int maxSectionIndexOnShelf = 0;
            if (catalogueDraft.getType() == CourseConstants.CataType.SECTION) {
                //小节最大编辑数
                CourseCatalogueDraft courseCatalogueDraft = chapterIdAndMaxSectionMap.get(catalogueDraft.getParentCatalogueId());
                maxIndexOnShelf = NumberUtils.null2Zero(
                        courseCatalogueDraft == null ? 0 : courseCatalogueDraft.getCIndex());
            } else if (catalogueDraft.getType() == CourseConstants.CataType.CHAPTER) {
                maxIndexOnShelf = maxChapterIndex;
                CourseCatalogueDraft courseCatalogueDraft = chapterIdAndMaxSectionMap.get(catalogueDraft.getId());
                maxSectionIndexOnShelf = NumberUtils.null2Zero(
                        courseCatalogueDraft == null ? 0 : courseCatalogueDraft.getCIndex());
            }
            vo.setIndex(catalogueDraft.getCIndex());
            vo.setMediaName(catalogueDraft.getVideoName());
            vo.setSubjectNum(NumberUtils.null2Zero(cataIdAndNumMap.get(catalogueDraft.getId())).intValue()); //练习总数量
            vo.setTotalScore(NumberUtils.null2Zero(cataIdAndTotalScoreMap.get(catalogueDraft.getId()))); //练习总分数
            vo.setMaxIndexOnShelf(maxIndexOnShelf);
            vo.setMaxSectionIndexOnShelf(maxSectionIndexOnShelf);
        }, new CourseCatalogDraftDataWrapper());
    }

    /**
     * 校验练习id列表
     * 1.校验本课程所有的练习都添加了题目
     * 2.校验上传的数据中有其他课程的数据，
     * 前端传过来的章节id列表为A，课程所有小节id和练习id列表为B，课程所有练习id列表为c
     * 原理：1. 判断A是不是B的子集，如果A不是B的子集，则前端传过来的id列表中有非当前课程的小节或练习
     * 2.判断c是A的子集，如果c不是A的子集，说明前端有漏传的该课程的练习id
     *
     * @param cataIds  前端传过来的小节id列表或者练习列表
     * @param courseId 课程id
     */
    private void checkPracticeIds(List<Long> cataIds, Long courseId) {
        //查询所有小节和练习的目录列表
        LambdaQueryWrapper<CourseCatalogueDraft> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CourseCatalogueDraft::getCourseId, courseId).in(CourseCatalogueDraft::getType,
                Arrays.asList(CourseConstants.CataType.SECTION, CourseConstants.CataType.PRATICE));
        List<CourseCatalogueDraft> courseCatalogueDrafts = baseMapper.selectList(queryWrapper);
        if (CollUtils.isEmpty(courseCatalogueDrafts)) {
            throw new BizIllegalException(CourseErrorInfo.Msg.COURSE_CATA_NOT_EXISTS);
        }
        //所有小节和练习的目录id列表
        List<Long> allCataIdList = courseCatalogueDrafts
                .stream()
                .map(CourseCatalogueDraft::getId)
                .collect(Collectors.toList());

        //判断前端传过来的章节id列表是该课程所有小节和练习集合的子集，
        // 如果不是子集，说明前端传过来的章节id不属于当前课程
        if (!CollUtils.containsAll(allCataIdList, cataIds)) {
            log.error("传过来了其他章节的章节id，courseId:{},cataIds:{}", courseId, cataIds);
            throw new BizIllegalException(CourseErrorInfo.Msg.COURSE_MEDIA_SAVE_ILLEGAL);
        }
        //所有练习的id列表
        List<Long> practiceIdList = courseCatalogueDrafts.stream().filter(practice -> practice.getType() == CourseConstants.CataType.PRATICE).
                map(CourseCatalogueDraft::getId).collect(Collectors.toList());
        //判断前端传过来的章节id列表是否符合是课程练习id父集合
        if (!CollUtils.containsAll(cataIds, practiceIdList)) {
            throw new BizIllegalException(CourseErrorInfo.Msg.COURSE_SUBJECT_SAVE_SUBJECT_IDS_NULL);
        }
    }

    /**
     * 校验小节或练习id列表
     * 1.校验本课程所有的小节都添加了视频
     * 2.校验上传的数据中有其他课程的数据，
     * 原理：1.先比较接口传递过来的数据和数据库中的小节总数量一致,不一致返回失败
     * 2.总数量一致，再求两者的交集，交集集合长度和前端传过来的数量一致，则通过
     *
     * @param cataIds  前端传过来的小节id列表或者练习列表
     * @param courseId 课程id
     */
    private void checkSectionIds(List<Long> cataIds, Long courseId) {
        //1.数据库小节查询条件
        LambdaQueryWrapper<CourseCatalogueDraft> queryWrapper =
                Wrappers.lambdaQuery(CourseCatalogueDraft.class)
                        .eq(CourseCatalogueDraft::getType, CourseConstants.CataType.SECTION)
                        .eq(CourseCatalogueDraft::getCourseId, courseId);
        //2.查询小节
        List<CourseCatalogueDraft> courseCatalogueDrafts = baseMapper.selectList(queryWrapper);

        //3.判断数据库中的小节和前端传来的小节数量是否一致
        if (CollUtils.size(courseCatalogueDrafts) != CollUtils.size(cataIds)) {
            throw new BizIllegalException(CourseErrorInfo.Msg.COURSE_MEDIA_SAVE_MEDIA_NULL);
        }
        //4.转化出数据库中课程小节id列表
        List<Long> cataIdsInDb =
                courseCatalogueDrafts
                        .stream()
                        .map(CourseCatalogueDraft::getId)
                        .collect(Collectors.toList());
        //5.取前端传来小节id列表和数据库中小节id列表交集
        Collection<Long> cataIdsOfIntersection = CollUtils.intersection(cataIds, cataIdsInDb);
        //6.判断小节交集id列表数量和前端传来小节id列表数量是否一致
        if (cataIdsOfIntersection.size() != cataIds.size()) {
            throw new BizIllegalException(CourseErrorInfo.Msg.COURSE_MEDIA_SAVE_MEDIA_NULL);
        }
    }

    /**
     * 根据课程id统计每个大章的媒资总时长
     *
     * @param courseId
     * @return
     */
    private List<CourseCatalogueDraft> calculateCatalogMediaDuration(Long courseId) {
        //1.查询条件
        LambdaQueryWrapper<CourseCatalogueDraft> queryWrapper =
                Wrappers.lambdaQuery(CourseCatalogueDraft.class)
                        .eq(CourseCatalogueDraft::getCourseId, courseId)
                        .eq(CourseCatalogueDraft::getType, CourseConstants.CataType.SECTION);
        //2.查询数据
        List<CourseCatalogueDraft> courseCatalogueDrafts = baseMapper.selectList(queryWrapper);
        if (CollUtils.isEmpty(courseCatalogueDrafts)) {
            return new ArrayList<>();
        }
        //3.统计每个章的课时总时长
        Map<Long, Integer> capthIdAndMediaDurationMap =
                courseCatalogueDrafts.stream().collect(Collectors.groupingBy(
                        CourseCatalogueDraft::getParentCatalogueId,
                        Collectors.summingInt(CourseCatalogueDraft::getMediaDuration)));
        //4.封装数据
        return capthIdAndMediaDurationMap.keySet()
                .stream()
                .map(key -> CourseCatalogueDraft.builder()
                        .id(key)
                        .mediaDuration(capthIdAndMediaDurationMap.get(key))
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * 课程目录草稿树型数据转换器
     */
    private class CourseCatalogDraftDataWrapper implements TreeDataUtils.DataProcessor<CataVO, CourseCatalogueDraft> {

        @Override
        public Object getParentKey(CourseCatalogueDraft courseCatalogueDraft) {
            return courseCatalogueDraft.getParentCatalogueId();
        }

        @Override
        public Object getKey(CourseCatalogueDraft courseCatalogueDraft) {
            return courseCatalogueDraft.getId();
        }

        @Override
        public Object getRootKey() {
            return 0L;
        }

        @Override
        public List<CataVO> getChild(CataVO cataVO) {
            return cataVO.getSections();
        }

        @Override
        public void setChild(CataVO parent, List<CataVO> child) {
            parent.setSections(child);
        }
    }
}
