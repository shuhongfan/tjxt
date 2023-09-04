package com.tianji.learning.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.tianji.common.enums.BaseEnum;
import lombok.Getter;

@Getter
public enum LessonStatus implements BaseEnum {
    NOT_BEGIN(0, "未学习"),
    LEARNING(1, "学习中"),
    FINISHED(2, "已学完"),
    EXPIRED(3, "已过期"),
    ;
    @JsonValue
    @EnumValue
    int value;
    String desc;

    LessonStatus(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static LessonStatus of(Integer value){
        if (value == null) {
            return null;
        }
        for (LessonStatus status : values()) {
            if (status.equalsValue(value)) {
                return status;
            }
        }
        return null;
    }
}
