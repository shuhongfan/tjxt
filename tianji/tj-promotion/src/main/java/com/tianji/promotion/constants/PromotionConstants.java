package com.tianji.promotion.constants;

public interface PromotionConstants {
    String COUPON_CODE_SERIAL_KEY = "coupon:code:serial";
    String COUPON_CODE_MAP_KEY = "coupon:code:map";
    String COUPON_CACHE_KEY_PREFIX = "prs:coupon:";
    String USER_COUPON_CACHE_KEY_PREFIX = "prs:user:coupon:";
    String COUPON_RANGE_KEY = "coupon:code:range";

    String[] RECEIVE_COUPON_ERROR_MSG = {
            "活动未开始",
            "库存不足",
            "活动已经结束",
            "领取次数过多",
    };
    String[] EXCHANGE_COUPON_ERROR_MSG = {
            "兑换码已兑换",
            "无效兑换码",
            "活动未开始",
            "活动已经结束",
            "领取次数过多",
    };
}
