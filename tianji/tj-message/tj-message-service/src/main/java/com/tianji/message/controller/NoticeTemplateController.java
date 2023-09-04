package com.tianji.message.controller;


import com.tianji.common.domain.dto.PageDTO;
import com.tianji.message.domain.dto.NoticeTemplateDTO;
import com.tianji.message.domain.dto.NoticeTemplateFormDTO;
import com.tianji.message.domain.query.NoticeTemplatePageQuery;
import com.tianji.message.service.INoticeTemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 通知模板 前端控制器
 * </p>
 *
 * @author 虎哥
 * @since 2022-08-19
 */
@RestController
@RequestMapping("/notice-templates")
@Api(tags = "通知模板管理接口")
@RequiredArgsConstructor
public class NoticeTemplateController {

    private final INoticeTemplateService noticeTemplateService;

    @PostMapping
    @ApiOperation("新增通知模板")
    public Long saveNoticeTemplate(@RequestBody NoticeTemplateFormDTO noticeTemplateFormDTO){
        return noticeTemplateService.saveNoticeTemplate(noticeTemplateFormDTO);
    }

    @PutMapping("/{id}")
    @ApiOperation("更新通知模板")
    public void updateNoticeTemplate(
            @RequestBody NoticeTemplateFormDTO noticeTemplateFormDTO,
            @ApiParam(value = "模板id", example = "1") @PathVariable("id") Long id){
        noticeTemplateFormDTO.setId(id);
        noticeTemplateService.updateNoticeTemplate(noticeTemplateFormDTO);
    }

    @GetMapping
    @ApiOperation("分页查询通知模板")
    public PageDTO<NoticeTemplateDTO> queryNoticeTemplates(NoticeTemplatePageQuery pageQuery){
        return noticeTemplateService.queryNoticeTemplates(pageQuery);
    }

    @GetMapping("/{id}")
    @ApiOperation("根据id查询模板")
    public NoticeTemplateDTO queryNoticeTemplate(@ApiParam(value = "模板id", example = "1") @PathVariable("id") Long id){
        return noticeTemplateService.queryNoticeTemplate(id);
    }
}
