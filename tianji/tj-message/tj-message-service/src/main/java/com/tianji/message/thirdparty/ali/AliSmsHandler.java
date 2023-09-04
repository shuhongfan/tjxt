package com.tianji.message.thirdparty.ali;

import com.aliyun.sdk.service.dysmsapi20170525.AsyncClient;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsResponseBody;
import com.tianji.api.dto.sms.SmsInfoDTO;
import com.tianji.common.utils.JsonUtils;
import com.tianji.common.utils.StringUtils;
import com.tianji.message.domain.po.MessageTemplate;
import com.tianji.message.thirdparty.ISmsHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service("aliYun")
@Slf4j
@RequiredArgsConstructor
public class AliSmsHandler implements ISmsHandler {

    private final AsyncClient asyncClient;

    @Override
    public void send(SmsInfoDTO platformSmsInfoDTO, MessageTemplate template) {
        log.info("aliYun平台，准备发送短信：{}", platformSmsInfoDTO);
        // 1.准备请求参数
        String phones = StringUtils.join(",", platformSmsInfoDTO.getPhones());
        SendSmsRequest request = SendSmsRequest.builder()
                .phoneNumbers(phones)
                .templateCode(template.getThirdTemplateCode())
                .signName(template.getSignName())
                .templateParam(JsonUtils.toJsonStr(platformSmsInfoDTO.getTemplateParams()))
                .build();
        // 2.发送短信验证码
        CompletableFuture<SendSmsResponse> responseFuture = asyncClient.sendSms(request);
        log.info("aliYun平台，短信发送请求发出 ...");
        // 3.结果处理
        responseFuture.thenAccept(response -> {
            SendSmsResponseBody body = response.getBody();
            String code = body.getCode();
            if("OK".equals(code)){
                log.debug("aliYun短信发送成功，手机号:{}", phones);
            }else{
                log.error("aliYun短信发送失败，code：{}， 原因：{}", code, body.getMessage());
            }
        });
        responseFuture.exceptionally(e -> {
            log.error("aliYun短信发送异常", e);
            return null;
        });
    }
}
