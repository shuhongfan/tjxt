package com.tianji.course.mapper;

import com.tianji.course.domain.po.CourseCatalogueDraft;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * <p>
 * 目录草稿 Mapper 接口
 * </p>
 *
 * @author wusongsong
 * @since 2022-07-19
 */
public interface CourseCatalogueDraftMapper extends BaseMapper<CourseCatalogueDraft> {

    String COLUMNS = "id, name, trailer, course_id, type, parent_catalogue_id, media_id, video_id, video_name, living_start_time, living_end_time, play_back, c_index, media_duration, dep_id, create_time, update_time, creater, updater";
    /**
     * 查询出需要更新到架上的目录数据
     */
    @Select("select id, name, trailer, course_id, type, parent_catalogue_id, media_id,  video_name, c_index, " +
            "media_duration, dep_id from course_catalogue_draft where course_id=#{courseId}")
    @ResultMap("BaseResultMap")
    List<CourseCatalogueDraft> getByCourseId(@Param("courseId") Long courseId);

    @Delete("<script>delete from course_catalogue_draft where course_id=#{courseId}" +
            " and type in (<foreach collection='types' item='type' separator=','>#{type}</foreach>)</script>")
    int deleteByCourseId(@Param("courseId") Long couseId, @Param("types")List<Integer> types);


    @Insert("insert into course_catalogue_draft(" + COLUMNS + ",can_update) " +
            "(select " + COLUMNS + ",0 from course_catalogue where course_id=#{courseId})" )
    int insertFromCourseCatalogue(@Param("courseId") Long courseId);

    @Select("SELECT id FROM course_catalogue_draft WHERE course_id=#{courseId} AND type IN (2, 3)")
    List<Long> getSectionIdByCourseId(Long courseId);
}
