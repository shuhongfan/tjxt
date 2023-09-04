package com.tianji.trade.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class ThreadPoolConfig {

    @Bean
    public ThreadPoolTaskExecutor sendRefundRequestExecutor(){
        ThreadPoolTaskExecutor refundExecutor = new ThreadPoolTaskExecutor();
        //配置核心线程数
        refundExecutor.setCorePoolSize(4);
        //配置最大线程数
        refundExecutor.setMaxPoolSize(20);
        //配置队列大小
        refundExecutor.setQueueCapacity(10000);
        //配置线程池中的线程的名称前缀
        refundExecutor.setThreadNamePrefix("pd-user-async-service-");
        // 由调用者线程执行
        refundExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        refundExecutor.initialize();
        return refundExecutor;
    }
}
