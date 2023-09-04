package com.tianji.search.enums;

import lombok.Getter;

@Getter
public enum CourseStatus {
    NOT_READY(1, "待上架"),
    ON_THE_MARKET(2, "已上架"),
    NO_LONGER_BE_SOLD(3, "下架"),
    EXPIRED(4, "已完结");
    ;
    int value;
    String desc;

    CourseStatus(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
