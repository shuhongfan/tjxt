package com.tianji.trade.constants;

import com.tianji.common.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RefundStatus implements BaseEnum {


    UN_APPROVE(1, "待审批", "提交退款申请"),
    CANCEL(2, "学员取消退款", "学员取消退款"),
    AGREE(3, "同意退款", "退款审批"),
    REJECT(4, "拒绝退款", "退款审批"),
    SUCCESS(5, "退款成功", "退款成功"),
    FAILED(6, "退款失败", "退款失败");

    private final int value;
    private final String desc;
    private final String progressName;

    public static RefundStatus of(Integer value){
        if(value == null){
            return null;
        }
        for (RefundStatus status : values()) {
            if(status.equalsValue(value)){
                return status;
            }
        }
        return null;
    }

    public static String desc(Integer value) {
        RefundStatus status = of(value);
        if (status == null) {
            return null;
        }
        return status.getDesc();
    }

    public static String progress(Integer value) {
        RefundStatus status = of(value);
        if (status == null) {
            return null;
        }
        return status.getProgressName();
    }

    public static boolean inProgress(Integer value) {
        if (value == null) {
            return false;
        }
        return UN_APPROVE.getValue() == value || AGREE.getValue() == value;
    }
}
