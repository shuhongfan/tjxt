package com.tianji.message.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.tianji.message.domain.po.SmsThirdPlatform;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@Configuration
@EnableConfigurationProperties(MessageProperties.class)
public class MessageConfig {
    @Bean("asyncNoticeExecutor")
    public Executor asyncNoticeExecutor() {
        log.info("开始初始化执行通知任务的线程池....");
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //配置核心线程数
        executor.setCorePoolSize(10);
        //配置最大线程数
        executor.setMaxPoolSize(15);
        //配置队列大小
        executor.setQueueCapacity(99999);
        //配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix("pd-user-async-service-");

        // 设置拒绝策略：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //执行初始化
        executor.initialize();
        log.info("初始化执行通知任务的线程池结束...");
        return executor;
    }
    @Bean("asyncSmsExecutor")
    public Executor asyncSmsExecutor() {
        log.info("开始初始化短信发送任务的线程池....");
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //配置核心线程数
        executor.setCorePoolSize(50);
        //配置最大线程数
        executor.setMaxPoolSize(100);
        //配置队列大小
        executor.setQueueCapacity(99999);
        //配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix("pd-user-async-service-");

        // 设置拒绝策略：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //执行初始化
        executor.initialize();
        log.info("初始化执行短信发送的线程池结束...");
        return executor;
    }

    @Bean
    public Cache<String, List<SmsThirdPlatform>> platformCache(){
        return Caffeine.newBuilder()
                .initialCapacity(1)
                .maximumSize(20_00)
                .expireAfterWrite(Duration.ofMinutes(30))
                .build();
    }
}
