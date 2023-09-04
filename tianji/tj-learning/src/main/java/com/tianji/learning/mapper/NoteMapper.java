package com.tianji.learning.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tianji.learning.domain.po.Note;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Set;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 虎哥
 */
public interface NoteMapper extends BaseMapper<Note> {

    Page<Note> queryNotePageBySectionId(
            Page<Note> p,
            @Param("userId") Long userId,
            @Param("courseId") Long courseId,
            @Param("sectionId")Long sectionId);

    Page<Note> queryNotePage(Page<Note> notePage, @Param("ew")  QueryWrapper<Note> wrapper);

    @Select("SELECT user_id FROM note WHERE gathered_note_id = #{id}")
    Set<Long> queryNoteGathers(Long id);
}
