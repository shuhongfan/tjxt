package com.tianji.common.utils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;

import java.lang.reflect.Field;

/**
 * 将查询条件对象、修改的查询条件转换为QueryWrapper
 * 查询wrapper {@link LambdaQueryWrapper}
 * 更新wrapper {@link LambdaUpdateWrapper}
 *
 * @ClassName SqlWrapperUtils
 * @author wusongsong
 * @since 2022/7/14 11:43
 * @version 1.0.0
 **/
public class SqlWrapperUtils {


    /**
     * 用于生成查询语句columns
     *
     * @param clazz
     * @param prefix
     * @return
     */
    public static String getSqlCoumns(Class<?> clazz, String prefix) {

        Field[] fields = ReflectUtils.getFields(clazz);
        if (ArrayUtils.isEmpty(fields)) {
            //如果该模型没有字段，给一个空，防止下次继续查询
            return null;
        }
        if (StringUtils.isNotEmpty(prefix)) {
            prefix = prefix + ".";
        }
        StringBuilder buffer = new StringBuilder();
        for (Field field : fields) {
            String fieldName = field.getName();
            if (ReflectUtils.getMethod(clazz, "get" + StringUtils.upperFirst(fieldName)) != null) {
                buffer.append(prefix).append(StringUtils.toUnderlineCase(fieldName))
                        .append(",");
            }
        }
        return buffer.substring(0, buffer.length() - 1);
    }

    /**
     * 将查询dto转换成LambdaQueryWrapper，目前支持equal查询
     *
     * @param queryDTO    sql查询条件dto
     * @param targetClazz 查询的数据库对应的po的class
     * @param <T>         查询的数据库对应的po的类型
     * @param <R>         需要转换的条件对象
     * @return LambdaQueryWrapper对象
     */
    public static <T, R> LambdaQueryWrapper<T> toLambdaQueryWrapper(R queryDTO, Class<T> targetClazz) {
        //sql查询条件
        LambdaQueryWrapper<T> queryWrapper = new LambdaQueryWrapper<>();
        T target = BeanUtils.toBean(queryDTO, targetClazz);
        queryWrapper.setEntity(target);
        return queryWrapper;
    }

    /**
     * 将查询dto转换成QueryWrapper，目前支持equal查询
     *
     * @param queryDTO    sql查询条件dto
     * @param targetClazz 查询的数据库对应的po的class
     * @param <T>         查询的数据库对应的po的类型
     * @param <R>         需要转换的条件对象
     * @return QueryWrapper对象
     */
    public static <T, R> QueryWrapper<T> toQueryWrapper(R queryDTO, Class<T> targetClazz) {
        //sql查询条件
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        T target = BeanUtils.toBean(queryDTO, targetClazz);
        queryWrapper.setEntity(target);
        return queryWrapper;
    }

    /**
     * 联表分页查询时使用，用来补充第二个表中的字段
     *
     * @param queryWrapper 查询QueryWrapper
     * @param prefix 查询条件前缀
     * @param queryDTO 查询实体
     * @param targetClasszz 目标class
     * @param <T> 目标类型
     * @param <R> 查询实体类型
     */
    public static <T, R> void suppleQueryWrapper(QueryWrapper<?> queryWrapper, String prefix, R queryDTO, Class<T> targetClasszz) {
        //将查询dto转换成目标对象
        T target = BeanUtils.toBean(queryDTO, targetClasszz);
        //获取目标对象的字段
        Field[] fields = ReflectUtils.getFields(targetClasszz);
        if (ArrayUtils.isNotEmpty(fields)) {
            //遍历所有字段
            for (Field field : fields) {
                //获取字段值
                Object value = ReflectUtils.getFieldValue(target, field);
                //字段有值，并且字段有get方法（确保该字段是数据库中对应的字段），将该字段加入到查询语句中
                if (value != null && ReflectUtils.getMethod(targetClasszz, "get" + StringUtils.upperFirst(field.getName())) != null) {
                    queryWrapper.eq(prefix + "." + StringUtils.toUnderlineCase(field.getName()), value);
                }
            }
        }
    }

    /**
     * 将更新dto转换成LambdaUpdateWrapper，目前支持equal查询，如果使用in或者日期的转换，使用convert处理
     *
     * @param updateDTO   sql更新条件dto
     * @param targetClazz 更新的数据库对应的po的class
     * @param <T>         更新的数据库对应的po的类型
     * @param <R>         需要转换的条件对象
     * @return LambdaQueryWrapper对象
     */
    public static <T, R> LambdaUpdateWrapper<T> toLambdaUpdateWrapper(R updateDTO, Class<T> targetClazz) {
        //sql查询条件
        LambdaUpdateWrapper<T> updateWrapper = new LambdaUpdateWrapper<>();
        T target = BeanUtils.toBean(updateDTO, targetClazz);
        updateWrapper.setEntity(target);
        return updateWrapper;
    }

}
