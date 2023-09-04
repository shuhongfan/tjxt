package com.tianji.promotion.strategy.discount;

import com.tianji.promotion.domain.po.Coupon;

/**
 * <p>优惠券折扣功能接口</p>
 */
public interface Discount {
    /**
     * 判断当前价格是否满足优惠券使用限制
     * @param totalAmount 订单总价
     * @param coupon 优惠券信息
     * @return 是否可以使用优惠券
     */
    boolean canUse(int totalAmount, Coupon coupon);

    /**
     * 计算折扣金额
     * @param totalAmount 总金额
     * @param coupon 优惠券信息
     * @return 折扣金额
     */
    int calculateDiscount(int totalAmount, Coupon coupon);

    /**
     * 根据优惠券规则返回规则描述信息
     * @return 规则描述信息
     */
    String getRule(Coupon coupon);
}
