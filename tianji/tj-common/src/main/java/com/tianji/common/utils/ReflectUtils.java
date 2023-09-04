package com.tianji.common.utils;

import cn.hutool.core.util.ReflectUtil;

/**
 * 反射工具
 **/
public class ReflectUtils extends ReflectUtil {

    /**
     * 判断一个类中是否含有指定字段
     *
     * @param fieldName 指定字段名称
     * @param clazz     类class
     * @return 是否包含 true/false
     */
    public static boolean containField(String fieldName, Class<?> clazz) {
        return getField(clazz, fieldName) != null;
    }
}
