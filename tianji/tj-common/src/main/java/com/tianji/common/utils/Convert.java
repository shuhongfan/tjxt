package com.tianji.common.utils;

/**
 * 对原对象进行计算，设置到目标对象中
 **/
public interface Convert<R,T>{
    void convert(R origin, T target);
}