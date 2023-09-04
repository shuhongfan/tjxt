package com.tianji.common.utils;

public class UserContext {
    private static final ThreadLocal<Long> TL = new ThreadLocal<>();

    /**
     * 保存用户信息
     * @param userId 用户id
     */
    public static void setUser(Long userId){
        TL.set(userId);
    }

    /**
     * 获取用户
     * @return 用户id
     */
    public static Long getUser(){
        return TL.get();
    }

    /**
     * 移除用户信息
     */
    public static void removeUser(){
        TL.remove();
    }
}
