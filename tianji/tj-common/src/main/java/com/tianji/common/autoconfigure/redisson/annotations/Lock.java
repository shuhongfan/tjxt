package com.tianji.common.autoconfigure.redisson.annotations;

import com.tianji.common.autoconfigure.redisson.enums.LockStrategy;
import com.tianji.common.autoconfigure.redisson.enums.LockType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * 分布式锁
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Lock {

    /**
     * 加锁key的表达式，支持SPEL表达式
     */
    String name();

    /**
     * 阻塞超时时长，不指定 waitTime 则按照Redisson默认时长
     */
    long waitTime() default 1;

    /**
     * 锁自动释放时长，默认是-1，其实是30秒 + watchDog模式
     */
    long leaseTime() default -1;

    /**
     * 时间单位，默认为秒
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * 如果设定了false,则方法结束不释放锁，而是等待leaseTime后自动释放
     */
    boolean autoUnlock() default true;

    /**
     * 锁的类型，包括：可重入锁、公平锁、读锁、写锁
     */
    LockType lockType() default LockType.DEFAULT;

    /**
     * 锁策略，包括5种，默认策略是 不断尝试获取锁，直到成功或超时，超时后抛出异常
     */
    LockStrategy lockStrategy() default LockStrategy.FAIL_AFTER_RETRY_TIMEOUT;
}
