package com.tianji.promotion.utils;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.api.RLock;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

@Component
@Aspect
@RequiredArgsConstructor
public class MyLockAspect implements Ordered {

    private final MyLockFactory lockFactory;

    @Around("@annotation(myLock)")
    public Object tryLock(ProceedingJoinPoint pjp, MyLock myLock) throws Throwable {
        // 1.创建锁对象
        RLock lock = lockFactory.getLock(myLock.lockType(), myLock.name());
        // 2.尝试获取锁
        boolean isLock = myLock.lockStrategy().tryLock(lock, myLock);
        // 3.判断是否成功
        if(!isLock) {
            // 3.1.失败，快速结束
            return null;
        }
        try {
            // 3.2.成功，执行业务
            return pjp.proceed();
        } finally {
            // 4.释放锁
            lock.unlock();
        }
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
