package com.tianji.course.mapper;

import com.tianji.course.domain.po.CourseTeacher;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 课程老师关系表草稿 Mapper 接口
 * </p>
 *
 * @author wusongsong
 * @since 2022-07-20
 */
public interface CourseTeacherMapper extends BaseMapper<CourseTeacher> {

    @Delete("delete from course_teacher where course_id=#{courseId}")
    int deleteByCourseId(@Param("courseId") Long courseId);

}
