package com.tianji.message.controller;


import com.tianji.common.domain.dto.PageDTO;
import com.tianji.message.domain.dto.NoticeTaskDTO;
import com.tianji.message.domain.dto.NoticeTaskFormDTO;
import com.tianji.message.domain.query.NoticeTaskPageQuery;
import com.tianji.message.service.INoticeTaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 系统通告的任务表，可以延期或定期发送通告 前端控制器
 * </p>
 *
 * @author 虎哥
 * @since 2022-08-19
 */
@Api(tags = "短信任务管理接口")
@RestController
@RequestMapping("/notice-tasks")
@RequiredArgsConstructor
public class NoticeTaskController {

    private final INoticeTaskService noticeTaskService;

    @PostMapping
    @ApiOperation("新增通知任务")
    public Long saveNoticeTask(@RequestBody NoticeTaskFormDTO noticeTaskFormDTO){
        return noticeTaskService.saveNoticeTask(noticeTaskFormDTO);
    }

    @PutMapping("/{id}")
    @ApiOperation("更新通知任务")
    public void updateNoticeTask(
            @RequestBody NoticeTaskFormDTO noticeTaskFormDTO,
            @ApiParam(value = "任务id", example = "1") @PathVariable("id") Long id){
        noticeTaskFormDTO.setId(id);
        noticeTaskService.updateNoticeTask(noticeTaskFormDTO);
    }

    @GetMapping
    @ApiOperation("分页查询通知任务")
    public PageDTO<NoticeTaskDTO> queryNoticeTasks(NoticeTaskPageQuery pageQuery){
        return noticeTaskService.queryNoticeTasks(pageQuery);
    }

    @GetMapping("/{id}")
    @ApiOperation("根据id查询任务")
    public NoticeTaskDTO queryNoticeTask(@ApiParam(value = "任务id", example = "1") @PathVariable("id") Long id){
        return noticeTaskService.queryNoticeTask(id);
    }
}
