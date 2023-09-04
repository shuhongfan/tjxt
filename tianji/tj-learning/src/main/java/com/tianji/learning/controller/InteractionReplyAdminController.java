package com.tianji.learning.controller;


import com.tianji.common.domain.dto.PageDTO;
import com.tianji.learning.domain.query.ReplyPageQuery;
import com.tianji.learning.domain.vo.ReplyVO;
import com.tianji.learning.service.IInteractionReplyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 互动提问的问题表 前端控制器
 * </p>
 *
 * @author 虎哥
 * @since 2022-10-21
 */
@Api(tags = "问题管理相关接口")
@RestController
@RequestMapping("/admin/replies")
@RequiredArgsConstructor
public class InteractionReplyAdminController {

    private final IInteractionReplyService replyService;

    @ApiOperation("隐藏或显示评论")
    @PutMapping("/{id}/hidden/{hidden}")
    public void hiddenReply(
            @ApiParam(value = "问题id", example = "1") @PathVariable("id") Long id,
            @ApiParam(value = "是否隐藏，true/false", example = "true") @PathVariable("hidden") Boolean hidden
    ){
        replyService.hiddenReply(id, hidden);
    }

    @ApiOperation("分页查询回答或评论")
    @GetMapping("page")
    public PageDTO<ReplyVO> queryReplyPage(ReplyPageQuery pageQuery){
        return replyService.queryReplyPage(pageQuery, true);
    }

    @ApiOperation("根据id查询回答或评论")
    @GetMapping("{id}")
    public ReplyVO queryReplyById(@ApiParam(value = "问题id", example = "1") @PathVariable("id") Long id){
        return replyService.queryReplyById(id);
    }
}
