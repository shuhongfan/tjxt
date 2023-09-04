package com.tianji.pay.third.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class RefundResponse {
    private Boolean success;
    /**
     * 退款渠道
     */
    private String channel;
    /**
     * 退款金额
     */
    private Integer amount;
    /**
     * 退款状态
     * @see RefundStatus
     */
    private Integer status;

    private String code;

    private String msg;

    public boolean refundSuccess(){
        return RefundStatus.SUCCESS.equalsValue(status);
    }
    public boolean refunding(){
        return RefundStatus.UN_KNOWN.equalsValue(status);
    }
    public boolean refundFailed(){
        return RefundStatus.FAILED.equalsValue(status);
    }
}
