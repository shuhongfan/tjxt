package com.tianji.promotion.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.tianji.common.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserCouponStatus implements BaseEnum {
    UNUSED(1, "未使用"),
    USED(2, "已使用"),
    EXPIRED(3, "已过期"),
    ;
    @EnumValue
    @JsonValue
    private final int value;
    private final String desc;
    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static UserCouponStatus of(Integer value) {
        if (value == null) {
            return null;
        }
        for (UserCouponStatus status : values()) {
            if (status.value == value) {
                return status;
            }
        }
        return null;
    }

    public static String desc(Integer value) {
        UserCouponStatus status = of(value);
        return status == null ? "" : status.desc;
    }
}
