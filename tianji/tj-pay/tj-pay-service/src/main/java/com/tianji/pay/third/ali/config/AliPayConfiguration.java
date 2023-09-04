package com.tianji.pay.third.ali.config;

import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.kernel.Config;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AliPayProperties.class)
public class AliPayConfiguration {

    @Bean
    public Config aliPayConfig(AliPayProperties properties) {
        Config config = new Config();
        config.protocol = properties.getProtocol();
        config.gatewayHost = properties.getGatewayHost();
        config.signType = properties.getSignType();
        config.appId = properties.getAppId();
        config.merchantPrivateKey = properties.getMerchantPrivateKey();
        config.alipayPublicKey = properties.getPublicKey();
        Factory.setOptions(config);
        return config;
    }
}
