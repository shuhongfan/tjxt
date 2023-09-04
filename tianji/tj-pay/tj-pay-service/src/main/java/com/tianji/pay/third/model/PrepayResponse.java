package com.tianji.pay.third.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 预支付下单的响应结果
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrepayResponse {
    /**
     * 是否成功
     */
    private boolean success;
    /**
     * 支付链接，用于生产二维码
     */
    private String payUrl;
    /**
     * 响应状态码
     */
    private String code;
    /**
     * 响应消息
     */
    private String msg;
    /**
     * 响应细节
     */
    private String detail;
}
