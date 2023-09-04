package com.tianji.course.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tianji.api.dto.IdAndNumDTO;
import com.tianji.course.domain.po.CourseDraft;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 草稿课程 Mapper 接口
 * </p>
 *
 * @author wusongsong
 * @since 2022-07-21
 */
public interface CourseDraftMapper extends BaseMapper<CourseDraft> {
    String COLUMNS = "id, name, course_type, cover_url, first_cate_id, second_cate_id, third_cate_id, free, price, template_type, template_url, status, purchase_start_time, purchase_end_time, step, media_duration,valid_duration, section_num, dep_id, create_time, update_time, creater, updater,score,publish_time";

    @Insert("insert into course_draft(" + COLUMNS + ",can_update) (select " + COLUMNS + ",0 from course where id=#{id} )")
    int insertFromCourse(@Param("id") Long id);

    /**
     * 批量查询老师所负责的课程数量
     * @param teacherIds
     * @return
     */
    @Select("<script>SELECT ct.teacher_id as id,count(*) as num " +
            " from course_draft c LEFT JOIN course_teacher_draft ct on c.id=ct.course_id " +
            "where c.status=1 and c.deleted=0 and ct.teacher_id in (<foreach collection='teacherIds' " +
            "item='teacherId' separator=','>#{teacherId}</foreach>)" +
            " GROUP BY ct.teacher_id</script>")
    List<IdAndNumDTO> countCourseNumOfTeacher(@Param("teacherIds")List<Long> teacherIds);

    /**
     * 统计草稿中其他课程
     * @param name
     * @return
     */
    @Select("<script>select count(*) from course_draft where <if test='id!=null'> id !=#{id} and </if> name=#{name} and deleted=0 </script>")
    int countByNameAndId(@Param("name") String name, @Param("id") Long id);
}
