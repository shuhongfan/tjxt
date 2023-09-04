package com.tianji.promotion.constants;

import com.tianji.common.enums.BaseEnum;
import com.tianji.promotion.domain.po.Coupon;
import com.tianji.promotion.strategy.discount.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DiscountType implements BaseEnum {
    PER_PRICE_DISCOUNT(1, "每满减"){
        @Override
        public Discount getDiscount(Coupon coupon) {
            return new PerPriceDiscount(
                    coupon.getDiscountValue(), coupon.getThresholdAmount(), coupon.getMaxDiscountAmount());
        }
    },
    RATE_DISCOUNT(2, "折扣"){
        @Override
        public Discount getDiscount(Coupon coupon) {
            return new RateDiscount(
                    coupon.getDiscountValue(), coupon.getThresholdAmount(), coupon.getMaxDiscountAmount());
        }
    },
    NO_THRESHOLD(3, "无门槛"){
        @Override
        public Discount getDiscount(Coupon coupon) {
            return new NoThresholdDiscount(coupon.getDiscountValue());
        }
    },
    PRICE_DISCOUNT(4, "满减"){
        @Override
        public Discount getDiscount(Coupon coupon) {
            return new PriceDiscount(coupon.getDiscountValue(), coupon.getThresholdAmount());
        }
    },
    ;
    private final int value;
    private final String desc;

    public static DiscountType of(Integer value) {
        if (value == null) {
            return null;
        }
        for (DiscountType status : values()) {
            if (status.value == value) {
                return status;
            }
        }
        return null;
    }

    public abstract Discount getDiscount(Coupon coupon);
}
