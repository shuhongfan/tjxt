package com.tianji.exam.constants;

import com.tianji.common.enums.BaseEnum;
import lombok.Getter;

@Getter
public enum QuestionType implements BaseEnum {
    // 1：单选题，2：多选题，3：不定向选择题，4：判断题，5：主观题
    RADIO(1, "单选题"),
    MULTI(2, "多选题"),
    UNCERTAINTY(3, "不定向选择题"),
    JUDGE(4, "判断题"),
    SUBJECTIVE(5, "主观题"),
    ;
    int value;
    String desc;

    QuestionType(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }


    public static QuestionType of(Integer value){
        if (value == null) {
            return null;
        }
        for (QuestionType status : values()) {
            if (status.equalsValue(value)) {
                return status;
            }
        }
        return null;
    }
}
