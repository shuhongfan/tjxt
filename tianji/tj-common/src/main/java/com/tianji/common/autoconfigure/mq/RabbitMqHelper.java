package com.tianji.common.autoconfigure.mq;

import cn.hutool.core.lang.UUID;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

import static com.tianji.common.constants.Constant.REQUEST_ID_HEADER;

@Slf4j
public class RabbitMqHelper {

    private final RabbitTemplate rabbitTemplate;
    private final MessagePostProcessor processor = new BasicIdMessageProcessor();
    private final ThreadPoolTaskExecutor executor;

    public RabbitMqHelper(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        executor = new ThreadPoolTaskExecutor();
        //配置核心线程数
        executor.setCorePoolSize(10);
        //配置最大线程数
        executor.setMaxPoolSize(15);
        //配置队列大小
        executor.setQueueCapacity(99999);
        //配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix("mq-async-send-handler");

        // 设置拒绝策略：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //执行初始化
        executor.initialize();
    }

    /**
     * 根据exchange和routingKey发送消息
     */
    public <T> void send(String exchange, String routingKey, T t) {
        log.debug("准备发送消息，exchange：{}， RoutingKey：{}， message：{}", exchange, routingKey,t);
        // 1.设置消息标示，用于消息确认，消息发送失败直接抛出异常，交给调用者处理
        String id = UUID.randomUUID().toString(true);
        CorrelationData correlationData = new CorrelationData(id);
        // 2.设置发送超时时间为500毫秒
        rabbitTemplate.setReplyTimeout(500);
        // 3.发送消息，同时设置消息id
        rabbitTemplate.convertAndSend(exchange, routingKey, t, processor, correlationData);
    }

    /**
     * 根据exchange和routingKey发送消息，并且可以设置延迟时间
     */
    public <T> void sendDelayMessage(String exchange, String routingKey, T t, Duration delay) {
        // 1.设置消息标示，用于消息确认，消息发送失败直接抛出异常，交给调用者处理
        String id = UUID.randomUUID().toString(true);
        CorrelationData correlationData = new CorrelationData(id);
        // 2.设置发送超时时间为500毫秒
        rabbitTemplate.setReplyTimeout(500);
        // 3.发送消息，同时设置消息id
        rabbitTemplate.convertAndSend(exchange, routingKey, t, new DelayedMessageProcessor(delay), correlationData);
    }


    /**
     * 根据exchange和routingKey 异步发送消息，并指定一个延迟时间
     *
     * @param exchange 交换机
     * @param routingKey 路由KEY
     * @param t 数据
     * @param <T> 数据类型
     */
    public <T> void sendAsyn(String exchange, String routingKey, T t, Long time) {
        String requestId = MDC.get(REQUEST_ID_HEADER);
        CompletableFuture.runAsync(()->{
            try {
                MDC.put(REQUEST_ID_HEADER, requestId);
                if(time != null && time > 0){
                    Thread.sleep( time);
                }
                send(exchange, routingKey, t);
            }catch (Exception e){
                log.error("推送消息异常，t:{},",t,e);
            }
        }, executor);
    }


    /**
     * 根据exchange和routingKey 异步发送消息
     *
     * @param exchange 交换机
     * @param routingKey 路由KEY
     * @param t 数据
     * @param <T> 数据类型
     */
    public <T> void sendAsyn(String exchange, String routingKey, T t){
        sendAsyn(exchange, routingKey, t, null);
    }

}
