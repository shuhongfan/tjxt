package com.tianji.gateway.config;

import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Set;

@Data
@Component
@ConfigurationProperties(prefix = "tj.auth")
public class AuthProperties implements InitializingBean {

    private Set<String> excludePath;

    @Override
    public void afterPropertiesSet() throws Exception {
        // 添加默认不拦截的路径
        excludePath.add("/error/**");
        excludePath.add("/jwks");
        excludePath.add("/accounts/login");
        excludePath.add("/accounts/admin/login");
        excludePath.add("/accounts/refresh");
    }
}
