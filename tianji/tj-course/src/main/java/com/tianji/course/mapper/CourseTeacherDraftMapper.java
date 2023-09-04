package com.tianji.course.mapper;

import com.tianji.course.domain.po.CourseTeacherDraft;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 课程老师关系表草稿 Mapper 接口
 * </p>
 *
 * @author wusongsong
 * @since 2022-07-20
 */
public interface CourseTeacherDraftMapper extends BaseMapper<CourseTeacherDraft> {

    String COLUMNS = "id, course_id, teacher_id, is_show, c_index, dep_id, create_time, update_time, creater, updater, deleted";
    /**
     * 根据课程id删除课程中的老师
     * @param courseId
     * @return
     */
    @Delete("delete from course_teacher_draft where course_id=#{courseId}")
    int deleteByCourseId(@Param("courseId") Long courseId);

    @Insert("insert into course_teacher_draft (" + COLUMNS + ") (select " + COLUMNS + " from course_teacher " +
            "where course_id= #{courseId} and deleted = 0)")
    int insertFromCourseTeacher(@Param("courseId") Long courseId);


}
