package com.tianji.api.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.tianji.api.cache.RoleCache;
import com.tianji.api.client.auth.AuthClient;
import com.tianji.api.dto.auth.RoleDTO;
import org.springframework.context.annotation.Bean;

import java.time.Duration;

public class RoleCacheConfig {
    /**
     * 角色的caffeine缓存
     */
    @Bean
    public Cache<Long, RoleDTO> roleCaches(){
        return Caffeine.newBuilder()
                .initialCapacity(1)
                .maximumSize(10_000)
                .expireAfterWrite(Duration.ofMinutes(30))
                .build();
    }
    /**
     * 角色的缓存工具
     */
    @Bean
    public RoleCache roleCache(Cache<Long, RoleDTO> roleCaches, AuthClient authClient){
        return new RoleCache(roleCaches, authClient);
    }
}
