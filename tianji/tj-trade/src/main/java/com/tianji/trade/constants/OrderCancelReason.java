package com.tianji.trade.constants;

import lombok.Getter;

@Getter
public enum OrderCancelReason {
    USER_CANCEL("用户取消订单"),
    TIME_OUT("订单超时取消"),
    ;
    private String msg;

    OrderCancelReason(String msg) {
        this.msg = msg;
    }
}
