package com.tianji.common.utils;

import cn.hutool.core.util.ArrayUtil;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 数组工具类
 * @ClassName ArrayUtils
 * @author wusongsong
 * @since 2022/7/10 12:02
 * @version 1.0.0
 **/
public class ArrayUtils extends ArrayUtil {



    /**
     * 将源数组转换成指定类型的列表
     *
     * @param originList  原始列表
     * @param targetClazz 转换后列表元素的类型
     * @param <R>         原始列表元素的类型
     * @param <T>         目标列表元素的类型
     * @return 目标类型的集合
     */
    public static <R, T> List<T> convert(R[] originList, Class<T> targetClazz) {
       return convert(originList, targetClazz, null);

    }

    /**
     * 将源数组转换成指定类型的列表
     *
     * @param originList  原始列表
     * @param targetClazz 转换后列表元素的类型
     * @param convert     转换特殊字段接口
     * @param <R>         原始列表元素的类型
     * @param <T>         目标列表元素的类型
     * @return 目标类型的集合
     */
    public static <R, T> List<T> convert(R[] originList, Class<T> targetClazz, Convert<R, T> convert) {
        if (isEmpty(originList)) {
            return null;
        }

        return Arrays.stream(originList)
                .map(origin -> BeanUtils.copyBean(origin, targetClazz, convert))
                .collect(Collectors.toList());

    }
}
