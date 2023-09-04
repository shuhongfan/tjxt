package com.tianji.common.validate;

/**
 * 实现后在接口访问时如果接口实现了这个接口
 * 会被自动自行接口check进行校验
 **/
public interface Checker<T> {

    /**
     * 用于实现validation不能校验的数据逻辑
     */
    default void check(){

    }

    default void check(T data){
    }
}