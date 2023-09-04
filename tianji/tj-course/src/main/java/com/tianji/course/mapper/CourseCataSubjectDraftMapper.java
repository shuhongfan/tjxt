package com.tianji.course.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tianji.course.domain.po.CataIdAndSubScore;
import com.tianji.course.domain.po.CourseCataSubjectDraft;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * <p>
 * 课程-题目关系表草稿 Mapper 接口
 * </p>
 *
 * @author wusongsong
 * @since 2022-07-20
 */
public interface CourseCataSubjectDraftMapper extends BaseMapper<CourseCataSubjectDraft> {

    @Insert("<script>insert into course_cata_subject_draft(course_id,cata_id,subject_id) value " +
            "<foreach collection='pos' item='po' separator=','>(#{po.courseId},#{po.cataId},#{po.subjectId})</foreach></script>")
    int batchInsert(@Param("pos")List<CourseCataSubjectDraft> courseCataSubjectDrafts);

    /**
     * 根据id删除课程对应的题目
     * @param courseId
     * @return
     */
    @Delete("delete from course_cata_subject_draft where course_id=#{courseId}")
    int deleteByCourseId(@Param("courseId") Long courseId);

    @Select("select course_id,cata_id,subject_id from course_cata_subject_draft where course_id=#{courseId}")
    @ResultMap("BaseResultMap")
    List<CourseCataSubjectDraft> getByCourseId(@Param("courseId") Long courseId);

    @Insert("insert into course_cata_subject_draft (course_id,cata_id,subject_id) " +
            "(select course_id,cata_id,subject_id from course_cata_subject where course_id=#{courseId})")
    int insertFromCourseCataSubject(@Param("courseId") Long courseId);

    @Select("select ccs.cata_id as cataId,ccs.subject_id as subjectId, s.score from course_cata_subject_draft ccs " +
            "left join subject s on ccs.subject_id=s.id where ccs.course_id=#{courseId}")
    List<CataIdAndSubScore> queryCataIdAndScoreByCorseId(@Param("courseId") Long courseId);

}
