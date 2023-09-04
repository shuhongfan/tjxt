package com.tianji.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tianji.api.client.exam.ExamClient;
import com.tianji.api.client.learning.LearningClient;
import com.tianji.api.client.trade.TradeClient;
import com.tianji.api.client.user.UserClient;
import com.tianji.api.dto.IdAndNumDTO;
import com.tianji.api.dto.course.*;
import com.tianji.api.dto.leanring.LearningLessonDTO;
import com.tianji.api.dto.leanring.LearningRecordDTO;
import com.tianji.api.dto.user.UserDTO;
import com.tianji.common.autoconfigure.mq.RabbitMqHelper;
import com.tianji.common.constants.ErrorInfo;
import com.tianji.common.constants.MqConstants;
import com.tianji.common.domain.dto.PageDTO;
import com.tianji.common.exceptions.BadRequestException;
import com.tianji.common.exceptions.DbException;
import com.tianji.common.utils.*;
import com.tianji.course.constants.CourseErrorInfo;
import com.tianji.course.constants.CourseStatus;
import com.tianji.course.constants.RedisContants;
import com.tianji.course.domain.dto.CoursePageQuery;
import com.tianji.course.domain.dto.CourseSimpleInfoListDTO;
import com.tianji.course.domain.po.Category;
import com.tianji.course.domain.po.Category3PO;
import com.tianji.course.domain.po.Course;
import com.tianji.course.domain.po.CourseTeacher;
import com.tianji.course.domain.vo.*;
import com.tianji.course.mapper.CourseDraftMapper;
import com.tianji.course.mapper.CourseMapper;
import com.tianji.course.mapper.CourseTeacherMapper;
import com.tianji.course.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 草稿课程 服务实现类
 * </p>
 *
 * @author wusongsong
 * @since 2022-07-20
 */
@Service
@Slf4j
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements ICourseService {

    @Autowired
    private CourseTeacherMapper courseTeacherMapper;

    @Autowired
    private CourseDraftMapper courseDraftMapper;

    @Autowired
    private ICourseDraftService courseDraftService;

    @Autowired
    private RabbitMqHelper rabbitMqHelper;

    @Autowired
    private ICourseCatalogueService courseCatalogueService;

    @Autowired
    private ICourseTeacherService courseTeacherService;

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private UserClient userClient;

    @Autowired
    private TradeClient tradeClient;

    @Autowired
    private ExamClient examClient;

    @Autowired
    private LearningClient learningClient;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {DbException.class, Exception.class})
    public void updateStatus(Long id, Integer status) {
        //1.组装数据
        Course course = new Course();
        course.setId(id);
        course.setStatus(status);
        course.setUpdateTime(LocalDateTime.now());
        //2.更新数据
        int result = baseMapper.updateById(course);
        //3.判断直接条数是1条
        if (result != 1) {
            throw new DbException(ErrorInfo.Msg.DB_UPDATE_EXCEPTION);
        }
    }

    @Override
    public CourseDTO getCourseDTOById(Long id) {
        // 1.查询课程信息
        Course course = baseMapper.selectById(id);
        //1.1判空
        if (course == null) {
            return null;
        }
        //2.教师查询条件
        LambdaQueryWrapper<CourseTeacher> queryWrapper =
                Wrappers.lambdaQuery(CourseTeacher.class)
                        .eq(CourseTeacher::getCourseId, id)
                        .orderBy(true, false, CourseTeacher::getCIndex)
                        .last(true, "limit 1");
        // 2.查询教师
        List<CourseTeacher> courseTeachers = courseTeacherMapper.selectList(queryWrapper);

        // 3.课程数据封装
        CourseDTO courseDTO = BeanUtils.toBean(course, CourseDTO.class);
        //3.1.一级课程分类
        courseDTO.setCategoryIdLv1(course.getFirstCateId());
        //3.2.二级课程分类
        courseDTO.setCategoryIdLv2(course.getSecondCateId());
        //3.3.三级课程分类
        courseDTO.setCategoryIdLv3(course.getThirdCateId());
        //3.4.媒资信息
        courseDTO.setDuration(course.getMediaDuration());
        //3.5.课程发布时间
        courseDTO.setPublishTime(course.getCreateTime());
        //3.6.课程小节数量
        courseDTO.setSections(course.getSectionNum());
        //3.7.课程第一位老师
        if (CollUtils.isNotEmpty(courseTeachers)) {
            courseDTO.setTeacher(courseTeachers.get(0).getTeacherId());
        } else {
            courseDTO.setTeacher(0L);
        }

        // 4.统计课程销量
        Map<Long, Integer> peoNumOfCourseMap = tradeClient.countEnrollNumOfCourse(CollUtils.singletonList(id));
        if (CollUtils.isNotEmpty(peoNumOfCourseMap)) {
            courseDTO.setSold(peoNumOfCourseMap.getOrDefault(id, 0));
        }
        //5.返回数据
        return courseDTO;

    }

    @Override
    public void delete(Long id) {
        //1.删除草稿信息
        courseDraftService.delete(id);
        //2.发送删除草稿mq
        rabbitMqHelper.send(MqConstants.Exchange.COURSE_EXCHANGE, MqConstants.Key.COURSE_DELETE_KEY, id);
    }

    @Override
    public List<CourseSimpleInfoDTO> getSimpleInfoList(CourseSimpleInfoListDTO courseSimpleInfoListDTO) {
        //1.课程查询条件
        LambdaQueryWrapper<Course> queryWrapper =
                Wrappers.lambdaQuery(Course.class)
                        .in(CollUtils.isNotEmpty(courseSimpleInfoListDTO.getThirdCataIds()),
                                Course::getThirdCateId, courseSimpleInfoListDTO.getThirdCataIds())
                        .in(CollUtils.isNotEmpty(courseSimpleInfoListDTO.getIds()),
                                Course::getId, courseSimpleInfoListDTO.getIds());
        //2.查询课程
        List<Course> courses = baseMapper.selectList(queryWrapper);
        //3.课程信息转化
        return BeanUtils.copyList(courses, CourseSimpleInfoDTO.class);
    }

    @Override
    public List<Course> queryByCategoryIdAndLevel(Long categoryId, Integer level) {
        //1.课程基本信息查询条件
        LambdaQueryWrapper<Course> queryWrapper =
                Wrappers.lambdaQuery(Course.class)
                        .eq(level == 1, Course::getFirstCateId, categoryId) //一级课程分类
                        .eq(level == 2, Course::getSecondCateId, categoryId) //二级课程分类
                        .eq(level == 3, Course::getThirdCateId, categoryId);//三级课程分类
        //2.查询课程基本信息
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public NameExistVO checkName(String name, Long id) {
        //1.正式课程同名课程数量
        LambdaQueryWrapper<Course> queryWrapper =
                Wrappers.lambdaQuery(Course.class)
                        .eq(Course::getName, name)
                        .last(id != null, " and id !=" + id);
        //2.统计数量
        Integer num = baseMapper.selectCount(queryWrapper);
        if (num > 0) {
            return NameExistVO.EXISTED;
        }
        //3.统计草稿课程同名数量
        return courseDraftService.checkName(name, id);
    }

    @Override
    public List<Long> queryExists(List<Long> idList, List<Integer> statusList) {
        //1.指定状态的课程id存在查询条件
        LambdaQueryWrapper<Course> queryWrapper =
                Wrappers.lambdaQuery(Course.class)
                        .in(Course::getId, idList)
                        .in(Course::getStatus, statusList);
        //2.根据条件查询课程
        List<Course> courses = baseMapper.selectList(queryWrapper);
        if (CollUtils.isEmpty(courses)) {
            return null;
        }
        //3.组装数据
        return courses.stream()
                .map(Course::getId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Long> queryCourseIdByName(String name) {
        // 1.查询条件
        LambdaQueryWrapper<Course> queryWrapper =
                Wrappers.lambdaQuery(Course.class)
                .like(Course::getName, name);
        // 2.查询数据
        List<Course> courses = baseMapper.selectList(queryWrapper);
        // 3.转换出课程id列表
        return courses.stream()
                .map(Course::getId)
                .collect(Collectors.toList());
    }


    @Override
    public CourseAndSectionVO queryCourseAndCatalogById(Long courseId) {
        // 1.获取当前用户
        Long userId = UserContext.getUser();
        // 2.查询课程详情
        CourseFullInfoDTO course = getInfoById(courseId, true, true);
        if (course == null) {
            return null;
        }
        // 3.组织VO
        CourseAndSectionVO vo = new CourseAndSectionVO();
        vo.setId(courseId);
        vo.setName(course.getName());
        vo.setSections(course.getSectionNum());
        vo.setCoverUrl(course.getCoverUrl());
        // 4.查询教师信息
        List<UserDTO> teachers = userClient.queryUserByIds(course.getTeacherIds());
        if (CollUtils.isNotEmpty(teachers)) {
            UserDTO teacher = teachers.get(0);
            vo.setTeacherName(teacher.getName());
            vo.setTeacherIcon(teacher.getIcon());
        }
        // 5.组装小节信息
        List<CatalogueDTO> catas = course.getChapters();
        List<ChapterVO> chapters = new ArrayList<>(catas.size());
        for (CatalogueDTO c : catas) {
            ChapterVO cv = new ChapterVO();
            cv.setId(c.getId());
            cv.setName(c.getName());
            cv.setIndex(c.getIndex());
            cv.setMediaDuration(c.getMediaDuration());
            List<SectionVO> sections = BeanUtils.copyToList(c.getSections(), SectionVO.class);
            cv.setSections(sections);
            chapters.add(cv);
        }
        vo.setChapters(chapters);
        // 6.查询学习进度
        if (learningClient == null) {
            return vo;
        }
        // 6.1.查询学习记录
        LearningLessonDTO lessonDTO = learningClient.queryLearningRecordByCourse(courseId);
        if (lessonDTO == null) {
            // 没有查询到课表信息，说明是免费试看，直接返回
            return vo;
        }
        vo.setLessonId(lessonDTO.getId());
        if (CollUtils.isEmpty(lessonDTO.getRecords())) {
            // 有课表信息，但是没有学习记录，不用处理进度问题了，直接返回
            return vo;
        }
        List<LearningRecordDTO> records = lessonDTO.getRecords();
        // 6.2.获取最近学习的记录。由于查询时按照学习时间排序，第一条记录就是最近学习的小节记录
        Long latestSectionId = lessonDTO.getLatestSectionId();
        if(latestSectionId == null) {
            latestSectionId = records.get(0).getSectionId();
        }
        vo.setLatestSectionId(latestSectionId);
        // 6.3.处理记录为一个map
        Map<Long, LearningRecordDTO> rMap = records.stream()
                .collect(Collectors.toMap(LearningRecordDTO::getSectionId, r -> r));
        // 6.4.填充学习进度到章节中
        for (ChapterVO chapter : vo.getChapters()) {
            for (SectionVO section : chapter.getSections()) {
                LearningRecordDTO r = rMap.get(section.getId());
                if (r == null) continue;
                section.setFinished(r.getFinished());
                section.setMoment(r.getMoment());
            }
        }
        return vo;
    }

    @Override
    public List<SubNumAndCourseNumDTO> countSubjectNumAndCourseNumOfTeacher(List<Long> teacherIds) {
        // 1.统计数据
        // 1.1.老师id和课程数量(已上架、已下架、已过期)
        Map<Long, Integer> teacherIdAndCourseNumMap =
                IdAndNumDTO.toMap(baseMapper.countCourseNumOfTeacher(teacherIds));
        // 1.2.待上架
        Map<Long, Integer> teacherIdAndCourseNumMap2 =
                IdAndNumDTO.toMap(courseDraftMapper.countCourseNumOfTeacher(teacherIds));
        // 1.3.统计老师的出题数量
        Map<Long, Integer> teacherIdAndSubjectNumMap = examClient.countSubjectNumOfTeacher(teacherIds);

        // 2.遍历老师id
        List<SubNumAndCourseNumDTO> subNumAndCourseNumDTOS = new ArrayList<>();
        for (Long teacherId : teacherIds) {
            subNumAndCourseNumDTOS.add(new SubNumAndCourseNumDTO(
                    //2.1.设置老师id
                    teacherId,
                    //2.2.设置老师课程数量
                    NumberUtils.null2Zero(teacherIdAndCourseNumMap.get(teacherId)) +
                            NumberUtils.null2Zero(teacherIdAndCourseNumMap2.get(teacherId)),
                    //2.3.设置老师出题数量
                    NumberUtils.null2Zero(teacherIdAndSubjectNumMap.get(teacherId))));
        }
        return subNumAndCourseNumDTOS;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int courseFinished() {
        //1.完结课程查询条件
        LambdaQueryWrapper<Course> queryWrapper =
                Wrappers.lambdaQuery(Course.class)
                        .le(Course::getPurchaseEndTime, LocalDateTime.now())
                        .in(Course::getStatus,
                                List.of(CourseStatus.DOWN_SHELF.getStatus(),
                                        CourseStatus.SHELF.getStatus()));

        //1.2.查询完结课程
        List<Course> courses = baseMapper.selectList(queryWrapper);
        //1.3.完结课程判空
        if (CollUtils.isEmpty(courses)) {
            return 0;
        }
        //2.组装数据
        List<Course> updateCourses = new ArrayList<>();
        for (Course course : courses) {
            Course updateCourse = new Course();
            //2.1.设置课程id
            updateCourse.setId(course.getId());
            //2.2.设置课程状态-已完结
            updateCourse.setStatus(CourseStatus.FINISHED.getStatus());
            updateCourses.add(updateCourse);
        }
        //3.批量完结课程
        updateBatchById(updateCourses);
        //4.发送课程完结mq
        sendFinishedCourse(courses);
        //5.清理草稿
        for (Course course: courses){
            courseDraftService.delete(course.getId());
        }

        return updateCourses.size();
    }

    @Override
    @Cacheable(cacheNames = RedisContants.Formatter.STATISTICS_COURSE_NUM_CATE)
    public Map<Long, Integer> countCourseNumOfCategory() {
        //1.统计课程分类拥有已上架、已完成的课程数量
        Map<Long, Integer> nomalCourseNumOfCategory =
                countNomalCourseNumOfCategory();
        //2.统计课程分类待上架，已下架的课程分类的数量
        Map<Long, Integer> draftCourseNumOfCategory =
                courseDraftService.countCourseNumOfCategory();
        //3.两组数据聚合
        return CollUtils.union(nomalCourseNumOfCategory, draftCourseNumOfCategory);
    }

    @Override
    @Cacheable(cacheNames = RedisContants.Formatter.CATEGORY_ID_LIST_HAVE_COURSE)
    public List<Long> getCategoryIdListWithCourse() {
        // 1.查询条件
        List<Category3PO> category3s = baseMapper.queryCategoryIdWithCourse();
        // 1.1.判空
        if(CollUtils.isEmpty(category3s)) {
            return new ArrayList<>();
        }
        // 2.将课程分类id设置到categoryIdList
        List<Long> categoryIdList = new ArrayList<>();
        category3s.stream().forEach(category3->{
            category3.setId(categoryIdList);
        });
        // 2.1.去重，并返回数据
        return categoryIdList.stream()
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public Integer countCourseNumOfCategory(Long categoryId) {
        //1.课程统计条件
        LambdaQueryWrapper<Course> queryWrapper =
                Wrappers.lambdaQuery(Course.class)
                        .or().eq(Course::getFirstCateId, categoryId)
                        .or().eq(Course::getSecondCateId, categoryId)
                        .or().eq(Course::getThirdCateId, categoryId);
        //2.统计课程数量
        return baseMapper.selectCount(queryWrapper);
    }

    @Override
    public CourseFullInfoDTO getInfoById(Long id, boolean withCatalogue, boolean withTeachers) {
        // 1.查询课程基本信息
        Course course = baseMapper.selectById(id);
        if (course == null) {
            throw new BadRequestException(CourseErrorInfo.Msg.COURSE_CHECK_NOT_EXISTS);
        }
        // 2.转换vo
        CourseFullInfoDTO courseFullInfoDTO = BeanUtils.toBean(course, CourseFullInfoDTO.class);

        // 3.查询目录信息
        if (withCatalogue) {
            courseFullInfoDTO.setChapters(courseCatalogueService.queryCourseCatalogues(id, true));
        }
        // 4.查询教师信息
        if (withTeachers) {
            courseFullInfoDTO.setTeacherIds(courseTeacherService.getTeacherIdOfCourse(id));
        }
        return courseFullInfoDTO;
    }

    @Override
    public PageDTO<CoursePageVO> queryForPage(CoursePageQuery coursePageQuery) {
        //1.课程查询条件
        LambdaQueryWrapper<Course> queryWrapper =
                SqlWrapperUtils.toLambdaQueryWrapper(coursePageQuery, Course.class);
        //1.1.课程条件-更新时间
        queryWrapper.between(
                ObjectUtils.isNotEmpty(coursePageQuery.getBeginTime())
                        && ObjectUtils.isNotEmpty(coursePageQuery.getEndTime()),
                Course::getUpdateTime,
                coursePageQuery.getBeginTime(),
                coursePageQuery.getEndTime());
        //1.2.课程查询条件-名称
        queryWrapper.like(
                StringUtils.isNotEmpty(coursePageQuery.getKeyword()),
                Course::getName,
                coursePageQuery.getKeyword());
        //1.3.分页查询数据
        Page<Course> page = page(coursePageQuery.toMpPage(), queryWrapper);
        //1.4.分页数据判空
        if (CollUtils.isEmpty(page.getRecords())) {
            return PageDTO.empty(page);
        }
        //2.课程更新人id列表
        List<Long> updaterList = page
                .getRecords()
                .stream()
                .map(Course::getUpdater)
                .collect(Collectors.toList());
        //2.1查询更新人用户信息
        List<UserDTO> userDTOS = userClient.queryUserByIds(updaterList);
        //2.2.转化课程更新人id+name map
        Map<Long, String> updaterMap =
                CollUtils.isEmpty(updaterList) ?
                        new HashMap<>()
                        : userDTOS
                        .stream()
                        .collect(Collectors.toMap(UserDTO::getId, UserDTO::getName));
        //3.获取所有课程分类信息
        List<Category> list = categoryService.list();
        //3.1.转化课程分类id+name map关系
        Map<Long, String> categoryNameMap =
                CollUtils.isEmpty(list) ?
                        new HashMap<>()
                        : list.stream()
                        .collect(Collectors.toMap(Category::getId, Category::getName));
        //4.课程id列表
        List<Long> courseIdList = page
                .getRecords()
                .stream()
                .map(Course::getId)
                .collect(Collectors.toList());
        //4.1.统计每个课程报名人数map关系
        Map<Long, Integer> peoNumOfCourseMap = tradeClient.countEnrollNumOfCourse(courseIdList);
        //5.组装数据
        return PageDTO.of(page, CoursePageVO.class, (course, coursePageVO) -> {
            //5.1.拼接课程分类名称
            String categories = StringUtils.format("{}/{}/{}",
                    categoryNameMap.get(course.getFirstCateId()),
                    categoryNameMap.get(course.getSecondCateId()),
                    categoryNameMap.get(course.getThirdCateId()));
            //5.2.设置课程分类名称
            coursePageVO.setCategories(categories);
            //5.3.设置课程更新人姓名
            coursePageVO.setUpdaterName(updaterMap.get(course.getUpdater()));
            //5.4.设置课程报名人数
            coursePageVO.setSold(NumberUtils.null2Zero(peoNumOfCourseMap.get(course.getId())));
            //5.5.设置课程小节数
            coursePageVO.setSections(course.getSectionNum());
        });
    }

    /**
     * 异步发送课程完结mq
     *
     * @param finishedCourse
     */
    private void sendFinishedCourse(List<Course> finishedCourse) {
        //1.遍历发送课程完结mq
        for (Course course : finishedCourse) {
            rabbitMqHelper.sendAsyn(MqConstants.Exchange.COURSE_EXCHANGE,
                    MqConstants.Key.COURSE_EXPIRE_KEY,
                    course.getId());
        }
    }

    /**
     * 统计课程分类上架和已完结课程的数量
     *
     * @return
     */
    private Map<Long, Integer> countNomalCourseNumOfCategory() {
        //1.查询条件
        LambdaQueryWrapper<Course> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Course::getStatus,
                Arrays.asList(CourseStatus.SHELF.getStatus(), CourseStatus.FINISHED.getStatus()));
        //2.查询数据
        List<Course> courses = baseMapper.selectList(queryWrapper);
        Map<Long, Integer> cateIdAndNumMap = new HashMap<>();
        //3.统计每个分类的课程数量
        for (Course course : courses) {
            //3.1一级分类数量
            Integer firstCateNum = cateIdAndNumMap.get(course.getFirstCateId());
            cateIdAndNumMap.put(course.getFirstCateId(), firstCateNum == null ? 1 : firstCateNum + 1);
            //3.2二级分类数量
            Integer secondCateNum = cateIdAndNumMap.get(course.getSecondCateId());
            cateIdAndNumMap.put(course.getSecondCateId(), secondCateNum == null ? 1 : secondCateNum + 1);
            //3.3三级分类数量够
            Integer thirdCateNum = cateIdAndNumMap.get(course.getThirdCateId());
            cateIdAndNumMap.put(course.getThirdCateId(), thirdCateNum == null ? 1 : thirdCateNum + 1);
        }
        return cateIdAndNumMap;
    }
}
