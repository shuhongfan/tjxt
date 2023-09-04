package com.tianji.pay.sdk.constants;

import com.tianji.common.enums.BaseEnum;
import lombok.Getter;

@Getter
public enum PayType implements BaseEnum {
    JSAPI(1, "网页支付JS"),
    MINI_APP(2, "小程序支付"),
    APP(3, "APP支付"),
    NATIVE(4, "扫码支付"),
    ;
    private final int value;
    private final String desc;

    PayType(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
