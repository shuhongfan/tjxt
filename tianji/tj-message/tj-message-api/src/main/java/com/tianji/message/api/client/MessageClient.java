package com.tianji.message.api.client;

import com.tianji.message.domain.dto.SmsInfoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("message-service")
public interface MessageClient {

    /**
     * 同步发送短信
     * @param smsInfoDTO 短信相关信息
     */
    @PostMapping("message")
    void sendMessage(@RequestBody SmsInfoDTO smsInfoDTO);

}
