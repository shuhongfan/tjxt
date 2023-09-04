package com.tianji.message.controller;

import com.tianji.api.dto.sms.SmsInfoDTO;
import com.tianji.message.service.ISmsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "短信发送控制器")
@RestController
@RequestMapping("sms")
@RequiredArgsConstructor
public class SmsController {

    private final ISmsService smsService;

    @ApiOperation("同步发送短信")
    @PostMapping("message")
    public void sendMessage(@RequestBody SmsInfoDTO smsInfoDTO){
        smsService.sendMessageAsync(smsInfoDTO);
    }
}
