package com.tianji.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tianji.api.dto.course.*;
import com.tianji.common.domain.dto.PageDTO;
import com.tianji.course.domain.dto.CoursePageQuery;
import com.tianji.course.domain.dto.CourseSimpleInfoListDTO;
import com.tianji.course.domain.po.Course;
import com.tianji.course.domain.vo.CourseAndSectionVO;
import com.tianji.course.domain.vo.CoursePageVO;
import com.tianji.course.domain.vo.NameExistVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 草稿课程 服务类
 * </p>
 *
 * @author wusongsong
 * @since 2022-07-20
 */
public interface ICourseService extends IService<Course> {

    /**
     * 修改课程状态
     *
     * @param id 课程id
     * @param status 课程状态
     */
    void updateStatus(Long id, Integer status);

    CourseDTO getCourseDTOById(Long id);


    void delete(Long id);

    /**
     * 根据条件查询课程简单信息
     *
     * @param courseSimpleInfoListDTO 课程三级分类列表
     * @return 课程信息列表
     */
    List<CourseSimpleInfoDTO> getSimpleInfoList(CourseSimpleInfoListDTO courseSimpleInfoListDTO);

    /**
     * 查询老师出题数目和课程数目
     * @param teacherIds 老师id
     * @return 老师的出题数量
     */
    List<SubNumAndCourseNumDTO> countSubjectNumAndCourseNumOfTeacher(List<Long> teacherIds);

    /**
     * 课程完结
     */
    int courseFinished();

    /**
     * 统计每个分类id所拥有的课程数量
     * @return 分类对应的课程数量
     */
    Map<Long, Integer> countCourseNumOfCategory();

    /**
     * 查询有已上架课程分类的分类id
     * @return
     */
    List<Long> getCategoryIdListWithCourse();

    /**
     * 统计某个课程分类的课程数量
     *
     * @param categoryId 课程分类id
     * @return 课程数量
     */
    Integer countCourseNumOfCategory(Long categoryId);

    /**
     * 查询课程的详细信息
     * @param id 课程id
     * @param withCatalogue 是否查询目录数据
     * @param withTeachers 是否查询教师数据
     * @return 课程详细信息
     */
    CourseFullInfoDTO getInfoById(Long id, boolean withCatalogue, boolean withTeachers);

    /**
     * 分页查询课程信息
     * @param coursePageQuery 分页参数
     * @return 课程分页数据
     */
    PageDTO<CoursePageVO> queryForPage(CoursePageQuery coursePageQuery);
    /**
     * 根据课程分类id查询课程列表
     * @param categoryId 课程分类id
     * @param level 课程分类级别
     * @return
     */
    List<Course> queryByCategoryIdAndLevel(Long categoryId, Integer level);

    /**
     * 校验名称是否存在，或者被其他课程占用
     * @param name 课程名称
     * @param id 当前课程名称
     */
    NameExistVO checkName(String name, Long id);

    /**
     * 查询课程id列表中
     * @param idList
     * @return
     */
    List<Long> queryExists(List<Long> idList,List<Integer> statusList);

    /**
     * 根据课程name模糊查询课程id列表
     * @param name
     * @return
     */
    List<Long> queryCourseIdByName(String name);

    CourseAndSectionVO queryCourseAndCatalogById(Long courseId);
}
