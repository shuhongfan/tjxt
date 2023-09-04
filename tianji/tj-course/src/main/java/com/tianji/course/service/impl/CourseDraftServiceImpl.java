package com.tianji.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tianji.api.client.exam.ExamClient;
import com.tianji.api.client.learning.LearningClient;
import com.tianji.api.client.trade.TradeClient;
import com.tianji.api.client.user.UserClient;
import com.tianji.api.dto.course.CourseDTO;
import com.tianji.api.dto.course.CoursePurchaseInfoDTO;
import com.tianji.api.dto.exam.QuestionBizDTO;
import com.tianji.api.dto.user.UserDTO;
import com.tianji.common.autoconfigure.mq.RabbitMqHelper;
import com.tianji.common.constants.ErrorInfo;
import com.tianji.common.constants.MqConstants;
import com.tianji.common.domain.dto.PageDTO;
import com.tianji.common.exceptions.BadRequestException;
import com.tianji.common.exceptions.BizIllegalException;
import com.tianji.common.exceptions.DbException;
import com.tianji.common.utils.*;
import com.tianji.course.constants.CourseConstants;
import com.tianji.course.constants.CourseErrorInfo;
import com.tianji.course.constants.CourseStatus;
import com.tianji.course.domain.dto.CourseBaseInfoSaveDTO;
import com.tianji.course.domain.dto.CoursePageQuery;
import com.tianji.course.domain.po.*;
import com.tianji.course.domain.vo.CourseBaseInfoVO;
import com.tianji.course.domain.vo.CoursePageVO;
import com.tianji.course.domain.vo.CourseSaveVO;
import com.tianji.course.domain.vo.NameExistVO;
import com.tianji.course.mapper.*;
import com.tianji.course.service.*;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidatorFactory;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 草稿课程 服务实现类
 * </p>
 *
 * @author wusongsong
 * @since 2022-07-18
 */
@Service
public class CourseDraftServiceImpl extends ServiceImpl<CourseDraftMapper, CourseDraft> implements ICourseDraftService {

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private ICourseService courseService;

    @Autowired
    private CourseContentDraftMapper courseContentDraftMapper;

    @Autowired
    private CourseContentMapper courseContentMapper;

    @Autowired
    private ValidatorFactory validatorFactory;

    @Autowired
    private ICourseCatalogueDraftService courseCatalogueDraftService;

    @Autowired
    private ICourseTeacherDraftService courseTeacherDraftService;

    @Autowired
    private CourseCatalogueDraftMapper courseCatalogueDraftMapper;

    @Autowired
    private CourseTeacherDraftMapper courseTeacherDraftMapper;

    @Autowired
    private CourseCataSubjectDraftMapper courseCataSubjectDraftMapper;

    @Autowired
    private UserClient userClient;

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private RabbitMqHelper rabbitMqHelper;

    @Autowired
    private TradeClient tradeClient;

    @Autowired
    private ExamClient examClient;

    @Autowired
    private LearningClient learningClient;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {DbException.class, Exception.class})
    public CourseSaveVO save(CourseBaseInfoSaveDTO courseBaseInfoSaveDTO) {
        List<Long> categoryIdList = null;
        Course course = null;
        //1.数据校验
        if (courseBaseInfoSaveDTO.getId() == null) {
            //1.1新增数据调起数据校验器
            ViolationUtils.process(validatorFactory.getValidator().validate(courseBaseInfoSaveDTO));
            //1.1.2.校验课程分类
            categoryIdList = categoryService.checkCategory(courseBaseInfoSaveDTO.getThirdCateId());
        } else {
            //1.2.未上架课程校验
            course = courseMapper.selectById(courseBaseInfoSaveDTO.getId());
            if (course == null) {
                //1.2.1.未上架课程校验请求参数
                ViolationUtils.process(validatorFactory.getValidator().validate(courseBaseInfoSaveDTO));
                //1.2.2.同名课程判空
                checkSameCourse(courseBaseInfoSaveDTO.getId(), courseBaseInfoSaveDTO.getName());
                //1.2.3.校验课程分类
                categoryIdList = categoryService.checkCategory(courseBaseInfoSaveDTO.getThirdCateId());
            }
        }

        CourseDraft courseDraft = new CourseDraft();
        //2.数据封装
        //2.1.content数据封装 课程介绍、课程细节、适用人群
        CourseContentDraft courseContentDraft = new CourseContentDraft();
        courseContentDraft.setCourseIntroduce(courseBaseInfoSaveDTO.getIntroduce());
        courseContentDraft.setCourseDetail(courseBaseInfoSaveDTO.getDetail());
        courseContentDraft.setUsePeople(courseBaseInfoSaveDTO.getUsePeople());
        //2.2.课程封面和课程下架时间
        courseDraft.setCoverUrl(courseBaseInfoSaveDTO.getCoverUrl());
        courseDraft.setPurchaseEndTime(courseBaseInfoSaveDTO.getPurchaseEndTime());
        //2.3.未上架数据封装，已上架课程不能修改字段
        if (course == null) {
            //2.3.1.课程价格
            courseDraft.setPrice(NumberUtils.null2Zero(courseBaseInfoSaveDTO.getPrice()));
            //2.3.2.课程有效期
            courseDraft.setValidDuration(courseBaseInfoSaveDTO.getValidDuration());
            //2.3.3.课程状态
            courseDraft.setStatus(CourseStatus.NO_UP_SHELF.getStatus());
            //2.3.4.一级课程分类id
            courseDraft.setFirstCateId(categoryIdList.get(0));
            //2.3.5.二级课程分类id
            courseDraft.setSecondCateId(categoryIdList.get(1));
            //2.3.6.三级课程分类id
            courseDraft.setThirdCateId(categoryIdList.get(2));
            //2.3.7.售卖模式
            courseDraft.setFree(courseBaseInfoSaveDTO.getFree() ? 1 : 0);
            //2.3.8.课程名称
            courseDraft.setName(courseBaseInfoSaveDTO.getName());
        }

        //3.操作
        if (courseBaseInfoSaveDTO.getId() == null) {
            //3.1.新增课程草稿
            //3.1.1.新生成课程id
            Long id = IdWorker.getId();
            //3.1.2.设置课程id
            courseContentDraft.setId(id);
            courseDraft.setId(id);
            //3.1.3.设置课程编辑进度
            courseDraft.setStep(CourseConstants.CourseStep.BASE_INFO);
            //3.1.4.插入课程草稿
            baseMapper.insert(courseDraft);
            //3.1.5.插入课程草稿内容
            courseContentDraftMapper.insert(courseContentDraft);
        } else {
            //3.2.编辑课程草稿
            //3.2.1.设置课程id
            courseContentDraft.setId(courseBaseInfoSaveDTO.getId());
            courseDraft.setId(courseBaseInfoSaveDTO.getId());
            //3.2.2.更新课程草稿
            baseMapper.updateById(courseDraft);
            //3.2.3.更新课程草稿内容
            courseContentDraftMapper.updateById(courseContentDraft);
        }
        //4.返回课程新增dto
        return CourseSaveVO
                .builder()
                .id(courseDraft.getId())
                .build();
    }

    @Override
    public CourseBaseInfoVO getCourseBaseInfo(Long id, Boolean see) {

        CourseBaseInfoVO courseBaseInfoVO = null;
        if (see) {
            //1.查询课程信息
            Course course = courseMapper.selectById(id);
            if (course != null) {
                //1.1.查询课程对应的报名购买人数和退款人数
                CoursePurchaseInfoDTO coursePurchaseInfoDTO = tradeClient.getPurchaseInfoOfCourse(id);
                //1.2.组装数据
                courseBaseInfoVO = BeanUtils.toBean(course, CourseBaseInfoVO.class);
                //1.3.查询课程内容
                CourseContent courseContent = courseContentMapper.selectById(id);
                //1.4.设置课程评分
                courseBaseInfoVO.setCoureScore(NumberUtils.div(NumberUtils.null2Zero(course.getScore()) * 1.0, 10, 2));
                //1.5.设置报名人数
                courseBaseInfoVO.setEnrollNum(coursePurchaseInfoDTO.getEnrollNum());
                //1.6.设置学习人数
                courseBaseInfoVO.setStudyNum(learningClient.countLearningLessonByCourse(id));
                //1.7.设置退款人数
                courseBaseInfoVO.setRefundNum(coursePurchaseInfoDTO.getRefundNum());
                //1.8.设置实付金额
                courseBaseInfoVO.setRealPayAmount(coursePurchaseInfoDTO.getRealPayAmount());
                //1.9.设置课程详情
                courseBaseInfoVO.setDetail(courseContent.getCourseDetail());
                //1.10.设置课程介绍
                courseBaseInfoVO.setIntroduce(courseContent.getCourseIntroduce());
                //1.11.设置课程适用人群
                courseBaseInfoVO.setUsePeople(courseContent.getUsePeople());
                //1.12.设置小节总数量
                courseBaseInfoVO.setCataTotalNum(course.getSectionNum());
            }
        }
        //2.查询草稿信息
        if (courseBaseInfoVO == null) {
            //2.1.查询草稿课程信息
            CourseDraft courseDraft = baseMapper.selectById(id);
            //2.2.有草稿课程信息
            if (courseDraft != null) {
                //2.3.组装课程信息
                courseBaseInfoVO = BeanUtils.toBean(courseDraft, CourseBaseInfoVO.class);
                //2.4.查询课程内容信息
                CourseContentDraft courseContentDraft = courseContentDraftMapper.selectById(id);
                //2.5.设置课程详情
                courseBaseInfoVO.setDetail(courseContentDraft.getCourseDetail());
                //2.6.设置课程介绍
                courseBaseInfoVO.setIntroduce(courseContentDraft.getCourseIntroduce());
                //2.7.适用人群
                courseBaseInfoVO.setUsePeople(courseContentDraft.getUsePeople());
                //2.8.课程章节数
                courseBaseInfoVO.setCataTotalNum(courseDraft.getSectionNum());
                //2.9.设置课程评分
                courseBaseInfoVO.setCoureScore(0d);
                //2.10.设置报名人数
                courseBaseInfoVO.setEnrollNum(0);
                //2.11.设置学习人数
                courseBaseInfoVO.setStudyNum(0);
                //2.12.设置退款人数
                courseBaseInfoVO.setRefundNum(0);
                //2.13.设置实付金额
                courseBaseInfoVO.setRealPayAmount(0);
            }
        }
        if(courseBaseInfoVO == null){
            return new CourseBaseInfoVO();
        }

        //3.查询创建者，更新者姓名
        List<UserDTO> userDTOS = userClient.queryUserByIds(
                Arrays.asList(courseBaseInfoVO.getCreater(), courseBaseInfoVO.getUpdater())
                        .stream()
                        .distinct()
                        .collect(Collectors.toList())
        );
        if (CollUtils.isNotEmpty(userDTOS)) {
            //3.1.创建者，更新至id+name映射关系
            Map<Long, String> operatorMap = userDTOS
                    .stream()
                    .collect(Collectors.toMap(UserDTO::getId, UserDTO::getName));
            //3.2.设置创建者姓名
            courseBaseInfoVO.setCreaterName(operatorMap.get(courseBaseInfoVO.getCreater()));
            //3.3.设置更新者姓名
            courseBaseInfoVO.setUpdaterName(operatorMap.get(courseBaseInfoVO.getUpdater()));
        }

        //4.课程分类信息
        List<Category> categories = categoryService.queryByIds(
                Arrays.asList(courseBaseInfoVO.getFirstCateId(),
                        courseBaseInfoVO.getSecondCateId(),
                        courseBaseInfoVO.getThirdCateId()));
        if (CollUtils.isNotEmpty(categories)) {
            //4.1分类id和名称关系
            Map<Long, String> categoryIdAndName = categories
                    .stream()
                    .collect(Collectors.toMap(Category::getId, Category::getName));
            //4.2.设置课程分类名称
            courseBaseInfoVO.setCateNames(
                    StringUtils.format("{}/{}/{}",
                            categoryIdAndName.get(courseBaseInfoVO.getFirstCateId()),
                            categoryIdAndName.get(courseBaseInfoVO.getSecondCateId()),
                            categoryIdAndName.get(courseBaseInfoVO.getThirdCateId()))
            );
        }
        return courseBaseInfoVO;
    }

    @Override
    public void updateStep(Long id, Integer step) {
        //1.查询课程草稿
        CourseDraft courseDraft = baseMapper.selectById(id);
        CourseDraft updateCourseDraft = new CourseDraft();
        updateCourseDraft.setId(id);
        updateCourseDraft.setCVersion(courseDraft.getCVersion() + 1);
        //2.设置课程步骤，课程步骤只能前进不能后退
        if (courseDraft.getStep() < step) {
            updateCourseDraft.setStep(step);
        }else {
            updateCourseDraft.setStep(courseDraft.getStep());
        }
        //3.设置课时数，保存目录和保存题目两部进行修改
        if (CourseConstants.CourseStep.CATALOGUE == step ||
                CourseConstants.CourseStep.SUBJECT == step) {
            //3.1题目保存和目录保存都会修改课时数量
            updateCourseDraft.setSectionNum(courseCatalogueDraftService.totalSectionNums(id));
        }
        //4.更新課程狀態
        baseMapper.updateById(updateCourseDraft);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {DbException.class, Exception.class})
    public void upShelf(Long id) {
        // 1.信息获取
        //1.1获取上架的课程草稿信息
        CourseDraft courseDraft = baseMapper.selectById(id);
        //1.2获取课程信息
        Course course = courseMapper.selectById(id);
        boolean isFirstUpShelf = (course == null);

        // 2.校验课程
        checkBeforeUpShelf(id);

        //3.计算每个章节的课时视频时长
        Map<Long, Integer> mediaDurations = courseCatalogueDraftService.calculateMediaDuration(id);
        //3.1.计算课程视屏总时长
        int totalMediaDuration = mediaDurations
                .values()
                .stream()
                .mapToInt(p -> p)
                .sum();


        //4.草稿信息上架到正式环境
        //4.1课程老师信息
        courseTeacherDraftService.copyToShelf(id, isFirstUpShelf);
        //4.2 题目信息上架
        courseCatalogueDraftService.copySubjectToShelf(id, isFirstUpShelf);
        //4.3目录信息上架
        courseCatalogueDraftService.copyToShelf(id, isFirstUpShelf);
        //4.4 组装课程基本信息、课程内容信息
        CourseContentDraft courseContentDraft = courseContentDraftMapper.selectById(id);
        CourseContent courseContent = BeanUtils.toBean(courseContentDraft, CourseContent.class);
        Course courseToShelf = BeanUtils.toBean(courseDraft, Course.class);
        //4.4.1.课程视频总时长
        courseToShelf.setMediaDuration(totalMediaDuration);
        //4.4.2.课程有效期月数
        courseToShelf.setValidDuration(courseDraft.getValidDuration());
        //4.4.3.课程发布时间
        courseToShelf.setPublishTime(DateUtils.now());
        //4.4.4.课程状态设置为已上架
        courseToShelf.setStatus(CourseStatus.SHELF.getStatus());
        //4.4.5.课程发布次数
        int publishTimes = (course == null) ?
                1 : NumberUtils.null2Zero(course.getPublishTimes()) + 1;
        courseToShelf.setPublishTimes(publishTimes);
        // 4.4.6.评分
        courseToShelf.setScore((int)(Math.random() * 10) + 40);

        //4.5.首次上架
        if (isFirstUpShelf) {
            //4.5.1.插入课程内容信息
            int result = courseContentMapper.insert(courseContent);
            if (result <= 0) {
                throw new DbException(ErrorInfo.Msg.DB_UPDATE_EXCEPTION);
            }
            //4.5.2.插入课程基本信息
            result = courseMapper.insert(courseToShelf);
            if (result <= 0) {
                throw new DbException(ErrorInfo.Msg.DB_UPDATE_EXCEPTION);
            }
            //4.5.1.删除课程草稿基本信息
            baseMapper.deleteById(id);
            //4.5.2.删除课程草稿内容信息
            courseContentDraftMapper.deleteById(id);
        } else {
            //4.6.再次上架
            //4.6.1.更新正式课程内容信息
            int result = courseContentMapper.updateById(courseContent);
            if (result <= 0) {
                throw new DbException(ErrorInfo.Msg.DB_UPDATE_EXCEPTION);
            }
            //4.6.2.更新正式课程基本信息
            result = courseMapper.updateVariableById(courseToShelf);
            if (result <= 0) {
                throw new DbException(ErrorInfo.Msg.DB_UPDATE_EXCEPTION);
            }
            //4.6.3.删除课程草稿基本信息
            baseMapper.deleteById(id);
            //4.6.4.删除课程草稿内容信息
            courseContentDraftMapper.deleteById(id);

        }
        //5.课程上架mq
        rabbitMqHelper.sendAsyn(MqConstants.Exchange.COURSE_EXCHANGE,
                MqConstants.Key.COURSE_UP_KEY,
                id,
                200L);
    }

    @Override
    public void checkBeforeUpShelf(Long id) {
        //1.获取上架的课程草稿信息
        CourseDraft courseDraft = baseMapper.selectById(id);
        //1.1.获取课程信息
        Course course = courseMapper.selectById(id);
        //2.课程校验
        //2.1.课程上架幂等校验
        if (courseDraft == null && course != null) {
            throw new BizIllegalException(CourseErrorInfo.Msg.COURSE_UP_SHELF_AREADY);
        }
        //2.2.课程id不存在的课程无法上架
        if (courseDraft == null && course == null) {
            throw new BizIllegalException(CourseErrorInfo.Msg.COURSE_UP_SHELF_NOT_FOUND_COURSE);

        }
        //2.3.草稿信息不完整无法上架
        if (courseDraft.getStep() != CourseConstants.CourseStep.TEACHER) {
            throw new BizIllegalException(CourseErrorInfo.Msg.COURSE_UP_SHELF_INFO_INCOMPLETE);
        }
        //课程
        //2.4.已上架/已完结课程不能上架
        if (course != null && course.getStatus() != CourseStatus.DOWN_SHELF.getStatus()) {
            throw new BizIllegalException(CourseErrorInfo.Msg.COURSE_UP_SHELF_STATE_WRONG);
        }
        //2.5.校验课程结束时间
        if(courseDraft.getPurchaseEndTime().isBefore(DateUtils.now())){
            throw new BizIllegalException(CourseErrorInfo.Msg.COURSE_UP_SHELF_PURCHASE_ILLEGAL);
        }
        //2.6.首次上架校验逻辑
        if (course == null) {
            //2.5.1.统计同名的课程数量
            int sameNameNum = courseMapper.countSameName(courseDraft.getName());
            //2.5.2.有同名课程不能上架
            if (sameNameNum > 0) {
                throw new BadRequestException(CourseErrorInfo.Msg.COURSE_SAVE_NAME_EXISTS);
            }
        }
        //2.7.校验课程目录
        courseCatalogueDraftService.checkCataInfoImplated(id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {DbException.class, Exception.class})
    public void downShelf(Long id) {
        //1.查询课程基本信息
        Course course = courseService.getById(id);
        //1.1课程状态判断
        if (course == null || !course.getStatus().equals(CourseStatus.SHELF.getStatus())) {
            throw new BizIllegalException(CourseErrorInfo.Msg.COURSE_DOWN_SHELF_FAILD);
        }
        //2.先更新课程状态
        courseService.updateStatus(id, CourseStatus.DOWN_SHELF.getStatus());
        //3.课程基本信息和内容信息copy到草稿中
        baseMapper.insertFromCourse(id);
        //4.课程内容copy到草稿中
        courseContentDraftMapper.insertFromCourseContent(id);
        //5.目录内容copy到草稿中
        courseCatalogueDraftMapper.insertFromCourseCatalogue(id);
        //6.课程题目copy到草稿中
        copySubject2Draft(id);
        //7.课程老师copy到草稿中
        courseTeacherDraftMapper.insertFromCourseTeacher(id);
        //8.下架mq广播
        rabbitMqHelper.send(MqConstants.Exchange.COURSE_EXCHANGE, MqConstants.Key.COURSE_DOWN_KEY, id);
    }

    @GlobalTransactional
    public void copySubject2Draft(Long courseId) {
        // 1.查询课程有关的小节信息
        List<Long> sectionIds = courseCatalogueDraftMapper.getSectionIdByCourseId(courseId);
        if (CollUtils.isEmpty(sectionIds)) {
            log.error("课程小节数据为空");
            return;
        }
        // 2.查询题目关系
        List<QuestionBizDTO> qbs = examClient.queryQuestionIdsByBizIds(sectionIds);
        if (CollUtils.isEmpty(qbs)) {
            return;
        }
        List<CourseCataSubjectDraft> list = qbs.stream().map(q -> new CourseCataSubjectDraft()
                .setCourseId(courseId).setCataId(q.getBizId()).setSubjectId(q.getQuestionId())
        ).collect(Collectors.toList());
        // 3.保存到草稿表
        courseCataSubjectDraftMapper.batchInsert(list);
    }

    @Override
    public CourseDTO getCourseDTOById(Long id) {
        //1.查询课程草稿基础信息
        CourseDraft courseDraft = baseMapper.selectById(id);
        //1.1.判空
        if (courseDraft == null) {
            return new CourseDTO();
        }
        //2.查询课程老师列表，并去序号的第一个
        LambdaQueryWrapper<CourseTeacherDraft> queryWrapper =
                Wrappers.lambdaQuery(CourseTeacherDraft.class)
                .eq(CourseTeacherDraft::getCourseId, id)
                .orderBy(true, false, CourseTeacherDraft::getCIndex)
                .last(true, "limit 1");
        //2.1.查询课程老师信息
        CourseTeacherDraft courseTeacherDraft = courseTeacherDraftMapper.selectOne(queryWrapper);
        //3.组装数据
        CourseDTO courseDTO = BeanUtils.toBean(courseDraft, CourseDTO.class);
        //3.1.设置课程分类，一级、二级、三级课程分类
        courseDTO.setCategoryIdLv1(courseDraft.getFirstCateId());
        courseDTO.setCategoryIdLv2(courseDraft.getSecondCateId());
        courseDTO.setCategoryIdLv3(courseDraft.getThirdCateId());
        //3.2.设置课程视频播放总时长
        courseDTO.setDuration(courseDraft.getMediaDuration());
        //3.3.设置课程总小节数
        courseDTO.setSections(courseDraft.getSectionNum());
        //3.4.设置课程教师id
        if (courseTeacherDraft != null) {
            courseDTO.setTeacher(courseTeacherDraft.getTeacherId());
        } else {
            courseDTO.setTeacher(0L);
        }

        return courseDTO;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {DbException.class, Exception.class})
    public void delete(Long id) {
        //1.删除课程草稿
        baseMapper.deleteById(id);
        //2.删除课程内容草稿
        courseContentDraftMapper.deleteById(id);
        //3.删除课程题目关系草稿
        courseCataSubjectDraftMapper.deleteByCourseId(id);
        //4.删除课程目录草稿
        courseCatalogueDraftMapper.deleteByCourseId(id, Arrays.asList(
                CourseConstants.CataType.CHAPTER,
                CourseConstants.CataType.SECTION,
                CourseConstants.CataType.PRATICE
        ));
        //5.删除课程老师关系草稿
        courseTeacherDraftMapper.deleteByCourseId(id);
    }

    @Override
    public PageDTO<CoursePageVO> queryForPage(CoursePageQuery coursePageQuery) {
        //1.课程草稿分页查询条件
        LambdaQueryWrapper<CourseDraft> queryWrapper =
                SqlWrapperUtils.toLambdaQueryWrapper(coursePageQuery, CourseDraft.class);
        //1.1课程查询条件-更新时间
        queryWrapper.between(
                ObjectUtils.isNotEmpty(coursePageQuery.getBeginTime()) &&
                        ObjectUtils.isNotEmpty(coursePageQuery.getEndTime()),
                CourseDraft::getUpdateTime,
                coursePageQuery.getBeginTime(),
                coursePageQuery.getEndTime());
        //1.2课程查询条件-搜索关键字
        queryWrapper.like(StringUtils.isNotEmpty(coursePageQuery.getKeyword()),
                CourseDraft::getName, coursePageQuery.getKeyword());
        //1.3.分页查询查询数据
        Page<CourseDraft> page = page(coursePageQuery.toMpPage(), queryWrapper);
        //1.4.分页查询结果判空
        if (CollUtils.isEmpty(page.getRecords())) {
            return PageDTO.empty(page);
        }
        //2.组装数据查询
        //2.1.课程更新人id列表
        List<Long> updaterList = page.getRecords().stream()
                .map(CourseDraft::getUpdater)
                .collect(Collectors.toList());
        //2.2.查询更新人用户信息
        List<UserDTO> userDTOS = userClient.queryUserByIds(updaterList);
        //2.3.转化更新人用户id+name 映射关系
        Map<Long, String> updaterMap =
                CollUtils.isEmpty(updaterList) ?
                        new HashMap<>() : userDTOS.stream().collect(Collectors.toMap(UserDTO::getId, UserDTO::getName));
        //2.4.查询课程分类列表
        List<Category> list = categoryService.list();
        //2.5.转化课程分类id+name映射关系
        Map<Long, String> categoryNameMap =
                CollUtils.isEmpty(list) ?
                        new HashMap<>() : list.stream().collect(Collectors.toMap(Category::getId, Category::getName));
        //2.6.课程id列表
        List<Long> courseIdList = page.getRecords().stream().map(CourseDraft::getId).collect(Collectors.toList());
        //2.7.统计课程报名人数map
        Map<Long, Integer> peoNumOfCourseMap = tradeClient.countEnrollNumOfCourse(courseIdList);
        //3.数据封装
        return PageDTO.of(page, CoursePageVO.class, (course, coursePageVO) -> {
            //3.1.拼接课程分类
            String categories = StringUtils.format("{}/{}/{}",
                    categoryNameMap.get(course.getFirstCateId()),
                    categoryNameMap.get(course.getSecondCateId()),
                    categoryNameMap.get(course.getThirdCateId()));
            //3.2.设置课程分类
            coursePageVO.setCategories(categories);
            //3.3.设置课程更新人
            coursePageVO.setUpdaterName(updaterMap.get(course.getUpdater()));
            //3.4.设置课程报名人数
            coursePageVO.setSold(NumberUtils.null2Zero(peoNumOfCourseMap.get(course.getId())));
            //3.5.设置课程总课时数
            coursePageVO.setSections(course.getSectionNum());
        });
    }

    @Override
    public NameExistVO checkName(String name, Long id) {
        //1.课程草稿同名查询条件
        LambdaQueryWrapper<CourseDraft> queryWrapper =
                Wrappers.lambdaQuery(CourseDraft.class)
                        .eq(CourseDraft::getName, name)
                        .last(id != null, " and id !=" + id);
        //2.统计同名课程数量
        Integer num = baseMapper.selectCount(queryWrapper);
        //3.返回同名课程VO
        return new NameExistVO(num > 0);
    }

    @Override
    public List<Long> queryExists(List<Long> idList) {
        //1.查询草稿课程基础信息列表
        List<CourseDraft> courses = baseMapper.selectBatchIds(idList);
        //1.1.草稿课程信息列表判空
        if (CollUtils.isEmpty(courses)) {
            return null;
        }
        //2.组装数据
        return courses.stream()
                .map(CourseDraft::getId)
                .collect(Collectors.toList());
    }

    @Override
    public Map<Long, Integer> countCourseNumOfCategory() {
        //1.查询所有课程草稿
        List<CourseDraft> courses = baseMapper.selectList(null);
        Map<Long, Integer> cateIdAndNumMap = new HashMap<>();
        //2.遍历统计每个课程分类拥有的课程数量
        for (CourseDraft course : courses) {
            //2.1.统计一级课程分类课程数量
            Integer firstCateNum = cateIdAndNumMap.get(course.getFirstCateId());
            cateIdAndNumMap.put(course.getFirstCateId(), firstCateNum == null ? 1 : firstCateNum + 1);
            //2.2.统计二级课程分类课程数量
            Integer secondCateNum = cateIdAndNumMap.get(course.getSecondCateId());
            cateIdAndNumMap.put(course.getSecondCateId(), secondCateNum == null ? 1 : secondCateNum + 1);
            //2.3.统计三级课程分类课程数量
            Integer thirdCateNum = cateIdAndNumMap.get(course.getThirdCateId());
            cateIdAndNumMap.put(course.getThirdCateId(), thirdCateNum == null ? 1 : thirdCateNum + 1);
        }
        return cateIdAndNumMap;
    }

    private void checkSameCourse(Long id, String name) {
        //1.查询正式环境是否有同名课程
        int countSameNameNum = courseMapper.countSameName(name);
        //1.1.同名课程数据判0
        if (countSameNameNum > 0) { //名称已经存在，提交时做双重校验
            throw new BadRequestException(CourseErrorInfo.Msg.COURSE_SAVE_NAME_EXISTS);
        }
        //2.查询正式环境是否有同名课程
        countSameNameNum = baseMapper.countByNameAndId(name, id);
        //2.1.同名课程数据判0
        if (countSameNameNum > 0) {
            throw new BadRequestException(CourseErrorInfo.Msg.COURSE_SAVE_NAME_EXISTS);
        }
    }
}
