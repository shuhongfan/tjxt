package com.tianji.pay.sdk.constants;

import com.tianji.common.utils.StringUtils;
import lombok.Getter;

@Getter
public enum PayChannel {
    wxPay("微信支付"),
    aliPay("支付宝支付"),
    ;

    private final String desc;

    PayChannel(String desc) {
        this.desc = desc;
    }

    public static String desc(String value){
        if (StringUtils.isBlank(value)) {
            return "";
        }
        return PayChannel.valueOf(value).getDesc();
    }
}
