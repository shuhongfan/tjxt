package com.tianji.pay.third.model;

import com.tianji.common.enums.BaseEnum;
import lombok.Getter;

@Getter
public enum PayStatus implements BaseEnum {
    NOT_COMMIT(0, "未提交"),
    WAIT_BUYER_PAY(1, "待支付"),
    TRADE_CLOSED(2, "已关闭"),
    TRADE_SUCCESS(3, "支付成功"),
    TRADE_FINISHED(3, "支付成功"),
    ;
    private final int value;
    private final String desc;

    PayStatus(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
