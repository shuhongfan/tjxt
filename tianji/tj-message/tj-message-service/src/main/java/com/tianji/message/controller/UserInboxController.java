package com.tianji.message.controller;


import com.tianji.common.domain.dto.PageDTO;
import com.tianji.message.domain.dto.UserInboxDTO;
import com.tianji.message.domain.dto.UserInboxFormDTO;
import com.tianji.message.domain.query.UserInboxQuery;
import com.tianji.message.service.IUserInboxService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 用户通知记录 前端控制器
 * </p>
 *
 * @author 虎哥
 * @since 2022-08-19
 */
@Api(tags = "用户收件箱接口")
@RestController
@RequestMapping("/inboxes")
@RequiredArgsConstructor
public class UserInboxController {

    private final IUserInboxService inboxService;

    @PostMapping
    @ApiOperation("发送私信")
    public Long sentMessageToUser(@RequestBody UserInboxFormDTO userInboxFormDTO){
        return inboxService.sentMessageToUser(userInboxFormDTO);
    }

    @ApiOperation("分页查询收件箱")
    @GetMapping
    public PageDTO<UserInboxDTO> queryUserInBoxesPage(UserInboxQuery query){
        return inboxService.queryUserInBoxesPage(query);
    }
}
