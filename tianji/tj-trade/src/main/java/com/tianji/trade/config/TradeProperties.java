package com.tianji.trade.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "tj.trade")
public class TradeProperties {
    /**
     * 单次允许购买的最大课程数量
     */
    private int maxCourseAmount = 10;
    /**
     * 订单支付的最大等待时间，单位分钟
     */
    private int payOrderTTLMinutes = 30;
}
