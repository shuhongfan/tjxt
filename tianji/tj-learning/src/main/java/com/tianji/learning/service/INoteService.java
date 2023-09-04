package com.tianji.learning.service;

import com.tianji.common.domain.dto.PageDTO;
import com.tianji.learning.domain.dto.NoteFormDTO;
import com.tianji.learning.domain.po.Note;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tianji.learning.domain.query.NoteAdminPageQuery;
import com.tianji.learning.domain.query.NotePageQuery;
import com.tianji.learning.domain.vo.NoteAdminDetailVO;
import com.tianji.learning.domain.vo.NoteAdminVO;
import com.tianji.learning.domain.vo.NoteVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 虎哥
 */
public interface INoteService extends IService<Note> {

    void saveNote(NoteFormDTO noteDTO);

    void gatherNote(Long id);

    void removeGatherNote(Long id);

    void updateNote(NoteFormDTO noteDTO);

    PageDTO<NoteVO> queryNotePage(NotePageQuery query);

    PageDTO<NoteAdminVO> queryNotePageForAdmin(NoteAdminPageQuery query);

    NoteAdminDetailVO queryNoteDetailForAdmin(Long id);

    void hiddenNote(Long id, boolean hidden);

    void removeMyNote(Long id);
}
