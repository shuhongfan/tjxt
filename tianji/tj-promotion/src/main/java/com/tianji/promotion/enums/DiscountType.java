package com.tianji.promotion.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.tianji.common.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DiscountType implements BaseEnum {
    PER_PRICE_DISCOUNT(1, "每满减"),
    RATE_DISCOUNT(2, "折扣"),
    NO_THRESHOLD(3, "无门槛"),
    PRICE_DISCOUNT(4, "满减"),
    ;
    @JsonValue
    @EnumValue
    private final int value;
    private final String desc;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static DiscountType of(Integer value) {
        if (value == null) {
            return null;
        }
        for (DiscountType status : values()) {
            if (status.value == value) {
                return status;
            }
        }
        return null;
    }
}
