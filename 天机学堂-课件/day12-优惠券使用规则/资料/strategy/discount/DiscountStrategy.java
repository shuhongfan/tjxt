package com.tianji.promotion.strategy.discount;

import com.tianji.promotion.enums.DiscountType;

import java.util.EnumMap;

public class DiscountStrategy {

    private final static EnumMap<DiscountType, Discount> strategies;

    static {
        strategies = new EnumMap<>(DiscountType.class);
        strategies.put(DiscountType.NO_THRESHOLD, new NoThresholdDiscount());
        strategies.put(DiscountType.PER_PRICE_DISCOUNT, new PerPriceDiscount());
        strategies.put(DiscountType.RATE_DISCOUNT, new RateDiscount());
        strategies.put(DiscountType.PRICE_DISCOUNT, new PriceDiscount());
    }

    public static Discount getDiscount(DiscountType type) {
        return strategies.get(type);
    }
}
