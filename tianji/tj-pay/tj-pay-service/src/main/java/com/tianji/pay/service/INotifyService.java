package com.tianji.pay.service;

import com.wechat.pay.contrib.apache.httpclient.notification.NotificationRequest;

import java.util.Map;

public interface INotifyService {
    void handleWxPayNotify(NotificationRequest request);

    void handleWxPayRefundNotify(NotificationRequest request);

    void handleAliPayNotify(Map<String, String> request);
}
