package com.tianji.promotion.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.tianji.common.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ObtainType implements BaseEnum {
    PUBLIC(1, "手动领取"),
    ISSUE(2, "发放兑换码"),
    ;
    @EnumValue
    @JsonValue
    private final int value;
    private final String desc;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static ObtainType of(Integer value) {
        if (value == null) {
            return null;
        }
        for (ObtainType status : values()) {
            if (status.value == value) {
                return status;
            }
        }
        return null;
    }
}
