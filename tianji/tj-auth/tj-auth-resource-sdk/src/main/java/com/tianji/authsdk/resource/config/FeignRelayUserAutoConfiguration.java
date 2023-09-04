package com.tianji.authsdk.resource.config;

import feign.Feign;
import com.tianji.authsdk.resource.interceptors.FeignRelayUserInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(Feign.class)
public class FeignRelayUserAutoConfiguration {

    @Bean
    public FeignRelayUserInterceptor feignRelayUserInterceptor(){
        return new FeignRelayUserInterceptor();
    }
}
