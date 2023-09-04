package com.tianji.media.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum Platform {
    TENCENT(1, "腾讯云", "/img-tx/"),
    ALI(2, "阿里云", "/img-ali/"),
    QI_NIU(3, "七牛云", "/img-qn/"),
    ;
    @EnumValue
    private final int value;
    private final String desc;
    private final String path;

    Platform(int value, String desc, String path) {
        this.value = value;
        this.desc = desc;
        this.path = path;
    }
}
