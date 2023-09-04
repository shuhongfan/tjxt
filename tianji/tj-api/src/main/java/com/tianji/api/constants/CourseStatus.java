package com.tianji.api.constants;

import com.tianji.common.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author wusongsong
 * @since 2022/7/18 16:07
 * @version 1.0.0
 **/
@Getter
@AllArgsConstructor
public enum CourseStatus implements BaseEnum {
    NO_UP_SHELF(1, "待上架"),
    SHELF(2, "已上架"),
    DOWN_SHELF(3, "下架"),
    FINISHED(4, "已完结");

    private final int value;
    private final String desc;

    public static String desc(Integer status) {
        if (status == null) {
            return "";
        }
        for (CourseStatus courseStatus : values()) {
            if (courseStatus.getValue() == status) {
                return courseStatus.getDesc();
            }
        }
        return null;
    }
}