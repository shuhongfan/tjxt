package com.tianji.trade.constants;

import com.tianji.common.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public enum OrderStatus implements BaseEnum {

    NO_PAY(1, "待支付", "下单"),
    PAYED(2, "已支付", "付款"),
    CLOSED(3, "已关闭", "交易关闭"),
    FINISHED(4, "已完成", "交易完成"),
    ENROLLED(5, "已报名", "免费报名"),
    REFUNDED(6, "申请退款", "申请退款");

    private final int value;
    private final String desc;
    private final String progressName;

    public static OrderStatus of(Integer value){
        if(value == null){
            return null;
        }
        for (OrderStatus status : values()) {
            if(status.equalsValue(value)){
                return status;
            }
        }
        return null;
    }

    public static String desc(Integer value) {
        OrderStatus status = of(value);
        if (status == null) {
            return null;
        }
        return status.getDesc();
    }

    public static String progress(Integer value) {
        OrderStatus status = of(value);
        if (status == null) {
            return null;
        }
        return status.getProgressName();
    }

    public static boolean canRefund(Integer value) {
        return PAYED.equalsValue(value);
    }
}
