package com.tianji.promotion.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MyLock {
    String name();

    long waitTime() default 1;

    long leaseTime() default -1;

    TimeUnit unit() default TimeUnit.SECONDS;

    MyLockType lockType() default MyLockType.RE_ENTRANT_LOCK;

    MyLockStrategy lockStrategy() default MyLockStrategy.FAIL_AFTER_RETRY_TIMEOUT;
}
