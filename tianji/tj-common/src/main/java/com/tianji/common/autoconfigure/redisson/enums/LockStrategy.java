package com.tianji.common.autoconfigure.redisson.enums;

import com.tianji.common.autoconfigure.redisson.annotations.Lock;
import com.tianji.common.exceptions.BizIllegalException;
import org.redisson.api.RLock;

public enum LockStrategy {
    /**
     * 不重试，直接结束，返回false
     */
    SKIP_FAST() {
        @Override
        public boolean tryLock(RLock lock, Lock properties) throws InterruptedException {
            return lock.tryLock(0, properties.leaseTime(), properties.timeUnit());
        }
    },
    /**
     * 不重试，直接结束，抛出异常
     */
    FAIL_FAST() {
        @Override
        public boolean tryLock(RLock lock, Lock properties) throws InterruptedException {
            boolean success = lock.tryLock(0, properties.leaseTime(), properties.timeUnit());
            if (!success) {
                throw new BizIllegalException("请求太频繁");
            }
            return true;
        }
    },
    /**
     * 重试，直到超时后，直接结束
     */
    SKIP_AFTER_RETRY_TIMEOUT() {
        @Override
        public boolean tryLock(RLock lock, Lock properties) throws InterruptedException {
            return lock.tryLock(properties.waitTime(), properties.leaseTime(), properties.timeUnit());
        }
    },
    /**
     * 重试，直到超时后，抛出异常
     */
    FAIL_AFTER_RETRY_TIMEOUT() {
        @Override
        public boolean tryLock(RLock lock, Lock properties) throws InterruptedException {
            boolean success = lock.tryLock(properties.waitTime(), properties.leaseTime(), properties.timeUnit());
            if (!success) {
                throw new BizIllegalException("请求超时");
            }
            return true;
        }
    },
    /**
     * 不停重试，直到成功为止
     */
    KEEP_RETRY() {
        @Override
        public boolean tryLock(RLock lock, Lock properties) throws InterruptedException {
            lock.lock(properties.leaseTime(), properties.timeUnit());
            return true;
        }
    },
    ;

    public abstract boolean tryLock(RLock lock, Lock properties) throws InterruptedException;
}
