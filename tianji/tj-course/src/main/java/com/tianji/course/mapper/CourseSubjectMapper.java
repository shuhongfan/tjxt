package com.tianji.course.mapper;

import com.tianji.course.domain.po.CourseSubject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 课程题目关系列表 Mapper 接口
 * </p>
 *
 * @author wusongsong
 * @since 2022-07-17
 */
public interface CourseSubjectMapper extends BaseMapper<CourseSubject> {

    @Insert("<script>insert into course_subject (course_id,subject_id) " +
            "value <foreach collection='courseSubjects' item='cs' separator=','>(#{cs.courseId},#{cs.subjectId})</foreach></script>")
    int batchInsert(@Param("courseSubjects")List<CourseSubject> courseSubjects);
}
