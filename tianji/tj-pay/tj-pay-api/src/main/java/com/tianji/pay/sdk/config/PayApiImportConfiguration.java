package com.tianji.pay.sdk.config;


import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.tianji.pay.sdk.client")
public class PayApiImportConfiguration {

}