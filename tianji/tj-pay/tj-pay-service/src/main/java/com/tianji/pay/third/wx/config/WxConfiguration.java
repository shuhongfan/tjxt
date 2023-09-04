package com.tianji.pay.third.wx.config;

import cn.hutool.crypto.PemUtil;
import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
import com.wechat.pay.contrib.apache.httpclient.auth.PrivateKeySigner;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Credentials;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Validator;
import com.wechat.pay.contrib.apache.httpclient.cert.CertificatesManager;
import com.wechat.pay.contrib.apache.httpclient.exception.HttpCodeException;
import com.wechat.pay.contrib.apache.httpclient.exception.NotFoundException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;

@Configuration
@EnableConfigurationProperties(WxPayProperties.class)
public class WxConfiguration {

    /**
     * 微信支付证书管理器
     */
    @Bean(destroyMethod = "stop")
    public CertificatesManager certificatesManager(WxPayProperties properties)
            throws HttpCodeException, GeneralSecurityException, IOException {
        // 1.加载私钥
        PrivateKey privateKey = PemUtil.readPemPrivateKey(
                new ByteArrayInputStream(properties.getPrivateKey().getBytes(StandardCharsets.UTF_8)));
        // 2.签名工具
        PrivateKeySigner privateKeySigner = new PrivateKeySigner(properties.getMchSerialNo(), privateKey);
        WechatPay2Credentials wechatPay2Credentials = new WechatPay2Credentials(properties.getMchId(), privateKeySigner);
        // 3.平台证书管理器
        CertificatesManager certificatesManager = CertificatesManager.getInstance();
        certificatesManager.putMerchant(
                properties.getMchId(), wechatPay2Credentials, properties.getApiV3Key().getBytes(StandardCharsets.UTF_8));
        return certificatesManager;
    }

    @Bean
    public CloseableHttpClient closeableHttpClient(WxPayProperties properties, CertificatesManager certificatesManager)
            throws NotFoundException {
        // 1.加载私钥
        PrivateKey privateKey = PemUtil.readPemPrivateKey(
                new ByteArrayInputStream(properties.getPrivateKey().getBytes(StandardCharsets.UTF_8)));
        // 2.初始化
        WechatPayHttpClientBuilder builder = WechatPayHttpClientBuilder.create()
                .withMerchant(properties.getMchId(), properties.getMchSerialNo(), privateKey)
                .withValidator(new WechatPay2Validator(certificatesManager.getVerifier(properties.getMchId())))
                ;

        // 3.构建
        return builder.build();
    }
}
