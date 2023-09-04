package com.tianji.common.autoconfigure.xxljob;

import com.tianji.common.utils.StringUtils;
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@ConditionalOnClass(XxlJobSpringExecutor.class)
@EnableConfigurationProperties(XxlJobProperties.class)
public class XxlJobConfig {

    @Bean
    public XxlJobSpringExecutor xxlJobExecutor(XxlJobProperties prop) {
        log.info(">>>>>>>>>>> xxl-job config init.");
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        XxlJobProperties.Admin admin = prop.getAdmin();
        if (admin != null && StringUtils.isNotEmpty(admin.getAddress())) {
            xxlJobSpringExecutor.setAdminAddresses(admin.getAddress());
        }
        XxlJobProperties.Executor executor = prop.getExecutor();
        if (executor != null) {
            if (executor.getAppName() != null)
                xxlJobSpringExecutor.setAppname(executor.getAppName());
            if (executor.getIp() != null)
                xxlJobSpringExecutor.setIp(executor.getIp());
            if (executor.getPort() != null)
                xxlJobSpringExecutor.setPort(executor.getPort());
            if (executor.getLogPath() != null)
                xxlJobSpringExecutor.setLogPath(executor.getLogPath());
            if (executor.getLogRetentionDays() != null)
                xxlJobSpringExecutor.setLogRetentionDays(executor.getLogRetentionDays());
        }
        if (prop.getAccessToken() != null)
            xxlJobSpringExecutor.setAccessToken(prop.getAccessToken());
        log.info(">>>>>>>>>>> xxl-job config end.");
        return xxlJobSpringExecutor;
    }
}
