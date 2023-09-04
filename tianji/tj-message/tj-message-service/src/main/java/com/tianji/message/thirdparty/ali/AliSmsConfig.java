package com.tianji.message.thirdparty.ali;

import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.sdk.service.dysmsapi20170525.AsyncClient;
import darabonba.core.client.ClientOverrideConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AliProperties.class)
public class AliSmsConfig {
    @Bean
    public AsyncClient asyncClient(AliProperties prop){
        // 1.访问凭证
        StaticCredentialProvider provider = StaticCredentialProvider.create(Credential.builder()
                .accessKeyId(prop.getAccessId())
                .accessKeySecret(prop.getAccessSecret())
                .build());
        // 2.创建客户端
        return AsyncClient.builder()
                .region("cn-shanghai") // Region ID
                .credentialsProvider(provider)
                .overrideConfiguration(
                        ClientOverrideConfiguration.create()
                                .setEndpointOverride("dysmsapi.aliyuncs.com")
                )
                .build();
    }
}
