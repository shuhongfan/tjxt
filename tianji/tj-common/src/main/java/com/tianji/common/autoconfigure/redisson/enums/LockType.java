package com.tianji.common.autoconfigure.redisson.enums;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

public enum LockType {
    DEFAULT(){
        @Override
        public RLock getLock(RedissonClient redissonClient, String name) {
            return redissonClient.getLock(name);
        }
    },
    FAIR_LOCK(){
        @Override
        public RLock getLock(RedissonClient redissonClient, String name) {
            return redissonClient.getFairLock(name);
        }
    },
    READ_LOCK(){
        @Override
        public RLock getLock(RedissonClient redissonClient, String name) {
            return redissonClient.getReadWriteLock(name).readLock();
        }
    },
    WRITE_LOCK(){
        @Override
        public RLock getLock(RedissonClient redissonClient, String name) {
            return redissonClient.getReadWriteLock(name).writeLock();
        }
    },
    ;

    public abstract RLock getLock(RedissonClient redissonClient, String name);
}
