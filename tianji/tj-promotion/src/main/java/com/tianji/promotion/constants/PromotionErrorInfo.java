package com.tianji.promotion.constants;

public interface PromotionErrorInfo {
    String COUPON_NOT_EXISTS = "优惠券不存在或活动已结束";
    String COUPON_EXPIRED = "优惠券已经过期";
    String COUPON_NOT_ENOUGH = "优惠券被领完了";

    String INVALID_COUPON_CODE = "兑换码无效或格式错误";
    String COUPON_ISSUE_EXPIRED = "优惠券已经停止发放";
    String INVALID_USER_COUPON = "用户优惠券不存在或已过期";
}
