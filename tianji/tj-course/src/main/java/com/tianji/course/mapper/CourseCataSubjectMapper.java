package com.tianji.course.mapper;

import com.tianji.course.domain.po.CataIdAndSubScore;
import com.tianji.course.domain.po.CourseCataSubject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 课程-题目关系表草稿 Mapper 接口
 * </p>
 *
 * @author wusongsong
 * @since 2022-07-20
 */
public interface CourseCataSubjectMapper extends BaseMapper<CourseCataSubject> {

    @Insert("<script>insert into course_cata_subject (course_id,cata_id,subject_id) value " +
            "<foreach collection='courseCataSubjects' item='ccs' separator=','>" +
            "(#{ccs.courseId},#{ccs.cataId},#{ccs.subjectId})" +
            "</foreach></script>")
    int batchInsert(@Param("courseCataSubjects")List<CourseCataSubject> courseCataSubjects);

    @Delete("delete from course_cata_subject where course_id=#{courseId}")
    int deleteByCourseId(@Param("courseId") Long courseId);

    @Select("select ccs.cata_id as cataId,ccs.subject_id as subjectId, s.score from course_cata_subject ccs " +
            "left join subject s on ccs.subject_id=s.id where ccs.course_id=#{courseId}")
    List<CataIdAndSubScore> queryCataIdAndScoreByCorseId(@Param("courseId") Long courseId);

    @Select("select subject_id from course_cata_subject where cata_id=#{cataId}")
    List<Long> querySubjectIdByCataId(@Param("cataId") Long cataId);


}
