package com.tianji.message.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "tj.message")
public class MessageProperties {
    /**
     * 通知的最大有效期，默认1个月
     */
    private Integer noticeTtlMonths = 1;
    /**
     * 私信的最大有效期，默认6个月
     */
    private Integer messageTtlMonths = 6;
}
