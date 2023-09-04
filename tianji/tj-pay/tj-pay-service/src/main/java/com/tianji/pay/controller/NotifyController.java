package com.tianji.pay.controller;

import com.tianji.common.utils.StringUtils;
import com.tianji.pay.sdk.constants.PayConstants;
import com.tianji.pay.service.INotifyService;
import com.wechat.pay.contrib.apache.httpclient.notification.NotificationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.stream.Collectors;

@ApiIgnore
@RestController
@RequestMapping("notify")
@RequiredArgsConstructor
public class NotifyController {

    private final INotifyService notifyService;
    /**
     * 支付宝支付的回调接口
     * @param httpRequest 回调参数
     * @return 处理结果
     */
    @PostMapping(PayConstants.ALI_CHANNEL_CODE)
    public ResponseEntity<String> handleAliPayNotify(HttpServletRequest httpRequest){
        // 1.处理请求参数为一个Map
        Map<String, String[]> parameterMap = httpRequest.getParameterMap();
        Map<String, String> request = parameterMap.entrySet().stream().collect(
                Collectors.toMap(Map.Entry::getKey, e -> StringUtils.join(",", e.getValue())));
        // 2.处理通知
        notifyService.handleAliPayNotify(request);
        return ResponseEntity.ok("success");
    }

    /**
     * 微信支付的回调接口
     * @return 处理结果
     */
    @PostMapping(PayConstants.WX_CHANNEL_CODE)
    public ResponseEntity<Object> handleWxPayNotify(HttpEntity<String> httpEntity){
        try {
            // 1.将请求信息写入 NotificationRequest
            NotificationRequest request = transformHttpEntityToNotificationRequest(httpEntity);
            // 2.处理通知
            notifyService.handleWxPayNotify(request);
        } catch (Exception e){
            return ResponseEntity.status(500).body(Map.of("code", "FAIL", "message", e.getMessage()));
        }
        // 3.返回成功
        return ResponseEntity.ok().build();
    }

    /**
     * 微信支付的回调接口
     * @return 处理结果
     */
    @PostMapping("/refund/" + PayConstants.WX_CHANNEL_CODE)
    public ResponseEntity<Object> handleWxPayRefundNotify(HttpEntity<String> httpEntity){
        try {
            // 1.将请求信息写入 NotificationRequest
            NotificationRequest request = transformHttpEntityToNotificationRequest(httpEntity);
            // 2.处理通知
            notifyService.handleWxPayRefundNotify(request);
        } catch (Exception e){
            return ResponseEntity.status(500).body(Map.of("code", "FAIL", "message", e.getMessage()));
        }
        // 3.返回成功
        return ResponseEntity.ok().build();
    }


    private NotificationRequest transformHttpEntityToNotificationRequest(HttpEntity<String> httpEntity) {
        HttpHeaders headers = httpEntity.getHeaders();
        // 1.构建通知请求信息
        return new NotificationRequest.Builder()
                .withSerialNumber(headers.getFirst("Wechatpay-Serial"))
                .withNonce(headers.getFirst("Wechatpay-Nonce"))
                .withTimestamp(headers.getFirst("Wechatpay-Timestamp"))
                .withSignature(headers.getFirst("Wechatpay-Signature"))
                .withBody(httpEntity.getBody())
                .build();
    }

}
