package com.tianji.learning.controller;

import com.tianji.common.domain.dto.PageDTO;
import com.tianji.learning.domain.dto.NoteFormDTO;
import com.tianji.learning.domain.query.NotePageQuery;
import com.tianji.learning.domain.vo.NoteVO;
import com.tianji.learning.service.INoteService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * <p>
 *  控制器
 * </p>
 *
 * @author 虎哥
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/notes")
public class NoteController {

    private final INoteService noteService;

    @ApiOperation("新增笔记")
    @PostMapping
    public void saveNote(@RequestBody NoteFormDTO noteDTO) {
        noteService.saveNote(noteDTO);
    }

    @ApiOperation("采集笔记")
    @PostMapping("/gathers/{id}")
    public void gatherNote(
            @ApiParam(value = "笔记id", example = "1") @PathVariable("id") Long id) {
        noteService.gatherNote(id);
    }

    @ApiOperation("取消采集笔记")
    @DeleteMapping("/gathers/{id}")
    public void removeGatherNote(
            @ApiParam(value = "笔记id", example = "1") @PathVariable("id") Long id) {
        noteService.removeGatherNote(id);
    }

    @ApiOperation("更新笔记")
    @PutMapping("/{id}")
    public void updateNote(
            @ApiParam(value = "笔记id", example = "1") @PathVariable("id") Long id,
            @RequestBody NoteFormDTO noteDTO) {
        noteDTO.setId(id);
        noteService.updateNote(noteDTO);
    }

    @ApiOperation("删除我的笔记")
    @DeleteMapping("/{id}")
    public void removeMyNote(@ApiParam(value = "笔记id", example = "1") @PathVariable("id") Long id) {
        noteService.removeMyNote(id);
    }

    @ApiOperation("用户端分页查询笔记")
    @GetMapping("/page")
    public PageDTO<NoteVO> queryNotePage(@Valid NotePageQuery query) {
        return noteService.queryNotePage(query);
    }
}
