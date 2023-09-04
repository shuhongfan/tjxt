package com.tianji.common.utils;

import cn.hutool.core.util.ObjectUtil;

import java.lang.reflect.Field;
import java.math.BigDecimal;

/**
 * Object操作工具
 **/
public class ObjectUtils extends ObjectUtil {

    /**
     * 为object设置默认值，对target中的基本类型进行默认值初始化,
     * 为null的对象不操作
     *
     * @param target 需要初始化的对象
     */
    public static void setDefault(Object target) {
        if (target == null) {
            return;
        }
        Class<?> clazz = target.getClass();
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            setDefault(field, target);
        }

    }

    /**
     * 给某个字段设置为默认值
     *
     * @param field
     * @param target
     */
    private static void setDefault(Field field, Object target) {
        field.setAccessible(true);
        try {
            Object value = field.get(target);
            if (value != null) {
                return;
            }
            String type = field.getGenericType().toString();
            Object defaultValue;
            switch (type) {
                case "class java.lang.String":
                case "class java.lang.Character":
                    defaultValue = "";
                    break;
                case "class java.lang.Double":
                    defaultValue = 0.0d;
                    break;
                case "class java.lang.Long":
                    defaultValue = 0L;
                    break;
                case "class java.lang.Short":
                    defaultValue = (short) 0;
                    break;
                case "class java.lang.Integer":
                    defaultValue = 0;
                    break;
                case "class java.lang.Float":
                    defaultValue = 0f;
                    break;
                case "class java.lang.Byte":
                    defaultValue = (byte) 0;
                    break;
                case "class java.math.BigDecimal":
                    defaultValue = BigDecimal.ZERO;
                    break;
                case "class java.lang.Boolean":
                    defaultValue = Boolean.FALSE;
                    break;
                default:
                    defaultValue = null;

            }
            field.set(target, defaultValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
