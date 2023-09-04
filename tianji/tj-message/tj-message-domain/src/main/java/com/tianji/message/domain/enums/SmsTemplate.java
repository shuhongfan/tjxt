package com.tianji.message.domain.enums;

import lombok.Getter;

@Getter
public enum SmsTemplate {
    VERIFY_CODE("短信验证码"),
    ;
    private String desc;

    SmsTemplate( String desc) {
        this.desc = desc;
    }
}
