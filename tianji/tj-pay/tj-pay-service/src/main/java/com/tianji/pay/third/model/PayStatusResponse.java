package com.tianji.pay.third.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class PayStatusResponse {
    private boolean success;
    /**
     * 业务状态码
     */
    private String code;
    /**
     * 业务消息
     */
    private String msg;
    /**
     * 支付单编号
     */
    private String payOrderNo;
    /**
     * 支付状态：参考:
     * @see PayStatus
     */
    private Integer payStatus;
    /**
     * 交易单的金额
     */
    private Integer totalAmount;
    /**
     * 支付成功时间
     */
    private LocalDateTime successTime;
}
