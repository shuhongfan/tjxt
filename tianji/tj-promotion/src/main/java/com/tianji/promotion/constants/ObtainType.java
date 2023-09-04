package com.tianji.promotion.constants;

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
    private final int value;
    @JsonValue
    private final String desc;

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

    public static String desc(Integer value) {
        ObtainType status = of(value);
        return status == null ? "" : status.desc;
    }
}
