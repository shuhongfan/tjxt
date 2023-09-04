package com.tianji.learning.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.tianji.common.enums.BaseEnum;
import lombok.Getter;

@Getter
public enum PointsRecordType implements BaseEnum {
    LEARNING(1, "课程学习", 50),
    SIGN(2, "每日签到", 0),
    QA(3, "课程问答", 20),
    NOTE(4, "课程笔记", 20),
    COMMENT(5, "课程评价", 0),
    ;
    @EnumValue
    @JsonValue
    int value;
    String desc;
    int maxPoints;

    PointsRecordType(int value, String desc, int maxPoints) {
        this.value = value;
        this.desc = desc;
        this.maxPoints = maxPoints;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static PointsRecordType of(Integer value){
        if (value == null) {
            return null;
        }
        for (PointsRecordType status : values()) {
            if (status.equalsValue(value)) {
                return status;
            }
        }
        return null;
    }
}