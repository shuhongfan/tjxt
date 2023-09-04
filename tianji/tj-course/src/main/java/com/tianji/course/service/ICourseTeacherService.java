package com.tianji.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tianji.course.domain.po.CourseTeacher;
import com.tianji.course.domain.vo.CourseTeacherVO;

import java.util.List;

/**
 * <p>
 * 课程老师关系表草稿 服务类
 * </p>
 *
 * @author wusongsong
 * @since 2022-07-20
 */
public interface ICourseTeacherService extends IService<CourseTeacher> {

    /**
     * 查询老师课程信息
     * @param couserId 课程id
     * @return 教师信息
     */
    List<CourseTeacherVO> queryTeachers(Long couserId);

    void deleteByCourseId(Long courserId);

    /**
     * 根据课程id获取老师id列表，并且排序
     * @param courseId 课程id
     * @return 教师信息
     */
    List<Long> getTeacherIdOfCourse(Long courseId);


}
