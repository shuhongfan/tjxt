package com.tianji.course.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tianji.course.domain.po.Category;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 课程分类 Mapper 接口
 * </p>
 *
 * @author wusongsong
 * @since 2022-07-14
 */
public interface CategoryMapper extends BaseMapper<Category> {

    String COLUMNS = "c.id,c.name,c.parent_id,c.level,c.priority,c.status,c.create_time,c.update_time,c.creater,c.updater,c.deleted";
    @Select("select " + COLUMNS + " from category c LEFT JOIN category c2 on c.id=c2.parent_id where c2.id=#{childId} limit 1")
    @ResultMap("BaseResultMap")
    Category getByChildId(@Param("childId") Long childId);
}
