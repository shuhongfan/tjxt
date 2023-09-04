package com.tianji.promotion.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.tianji.common.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CouponStatus implements BaseEnum {
    DRAFT(1, "待发放"),
    UN_ISSUE(2, "未开始"),
    ISSUING(3, "发放中"),
    FINISHED(4, "发放结束"),
    PAUSE(5, "暂停");
    @JsonValue
    @EnumValue
    private final int value;
    private final String desc;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static CouponStatus of(Integer value) {
        if (value == null) {
            return null;
        }
        for (CouponStatus status : values()) {
            if (status.value == value) {
                return status;
            }
        }
        return null;
    }

    public static String desc(Integer value) {
        CouponStatus status = of(value);
        return status == null ? "" : status.desc;
    }
}
