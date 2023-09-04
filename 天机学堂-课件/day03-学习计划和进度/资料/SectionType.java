package com.tianji.learning.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.tianji.common.enums.BaseEnum;
import lombok.Getter;

@Getter
public enum SectionType implements BaseEnum {
    VIDEO(1, "视频"),
    EXAM(2, "考试"),
    ;
    @JsonValue
    @EnumValue
    int value;
    String desc;

    SectionType(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }


    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static SectionType of(Integer value){
        if (value == null) {
            return null;
        }
        for (SectionType status : values()) {
            if (status.equalsValue(value)) {
                return status;
            }
        }
        return null;
    }
}
