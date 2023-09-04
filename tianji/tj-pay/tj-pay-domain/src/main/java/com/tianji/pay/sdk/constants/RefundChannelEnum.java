package com.tianji.pay.sdk.constants;

import com.tianji.common.utils.StringUtils;
import lombok.Getter;

@Getter
public enum RefundChannelEnum {
    COUPON("支付宝红包"),
    ALIPAYACCOUNT("支付宝账户"),
    POINT("集分宝"),
    DISCOUNT("折扣券"),
    PCARD("预付卡"),
    MCARD("商家储值卡"),
    MDISCOUNT("商户优惠券"),
    MCOUPON("商户红包"),
    PCREDIT("蚂蚁花呗"),
    BANKCARD("银行卡"),
    MONEYFUND("余额宝"),
    VOUCHER("券"),
    ORIGINAL("原路退款"),
    BALANCE("退回到余额"),
    OTHER_BALANCE("其他余额账户"),
    OTHER_BANKCARD("其他银行卡"),
    ;
    private final String desc;

    RefundChannelEnum(String desc) {
        this.desc = desc;
    }

    public static String desc(String value){
        if(StringUtils.isBlank(value)){
            return "";
        }
        return RefundChannelEnum.valueOf(value).getDesc();
    }
}
