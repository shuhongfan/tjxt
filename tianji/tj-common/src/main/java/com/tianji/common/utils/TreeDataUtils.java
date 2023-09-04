package com.tianji.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 树形数据处理工具
 *
 * @ClassName TreeDataUtils
 * @author wusongsong
 * @since 2022/7/19 14:31
 * @version 1.0.0
 **/
@Slf4j
public class TreeDataUtils {


    /**
     * 遍历树状数据计算目标
     * @param data
     * @param calculateDataProcessor
     * @param <T>
     * @return
     */
    public static <T> Object ergodicTreeCalculate(List<T> data, CalculateDataProcessor<T> calculateDataProcessor) {
        if(CollUtils.isEmpty(data)){
            return null;
        }
        Object result = null;
        for (T t :data ) {
            Object tData = calculateDataProcessor.getData(t);
            List<T> childData = calculateDataProcessor.getChildData(t);
            Object currentAllData = calculateDataProcessor.calculate(tData, ergodicTreeCalculate(childData, calculateDataProcessor));
            calculateDataProcessor.setResult(t, currentAllData);
            result = calculateDataProcessor.calculate(result, currentAllData);
        }
        return result;
    }

    /**
     * 将树状数据转化成目标类型的列表数据，并建立数据之间的父子关系，采用了递归
     *
     * @param parentKey 父数据key
     * @param originData 原始树状数据
     * @param dataProcessor 数据转换器
     * @param clazz 目标类型
     * @param convert 原始数据转换成目标数据的转换器
     * @param targetData 目标数据放入到该列表中，该字段不能为空
     * @param <T> 目标数据类型
     * @param <R> 原始数据类型
     */
    public static <T, R> void parseTreeToList(Object parentKey, List<R> originData,
                                              ToListDataProcessor<T, R> dataProcessor, Class<T> clazz,
                                              Convert<R, T> convert, List<T> targetData, Filter<R> filter) {
        if (CollUtils.isNotEmpty(originData)) {
            for (R data : originData) {
                T target = BeanUtils.copyBean(data, clazz, convert);
                dataProcessor.setParent(target, parentKey);
                targetData.add(target);
                parseTreeToList(dataProcessor.getKey(data), dataProcessor.getChildren(data), dataProcessor, clazz, convert, targetData, filter);
            }
        }
    }

    /**
     * 根据数据之间的父子关系将原始数据列表转化成树型数据，并将数据转化成目标类型
     * 适用场景 每一条数据都有一个唯一标识和父数据的唯一标识
     *
     * @param originData    原始数据，列表
     * @param clazz         目标类型class
     * @param dataProcessor 树形数据包装器
     * @param <T>           目标数据的类型
     * @param <R>           原始数据的类型
     * @return 目标数据类型的树状数据
     */
    public static <T, R> List<T> parseToTree(List<R> originData, Class<T> clazz, DataProcessor<T, R> dataProcessor) {
        return parseToTree(originData, clazz, null, dataProcessor, new DefaultFilter());
    }

    /**
     * 根据数据之间的父子关系将原始数据列表转化成树型数据，并将数据转化成目标类型
     * 适用场景 每一条数据都有一个唯一标识和父数据的唯一标识
     *
     * @param originData    原始数据，列表
     * @param clazz         目标类型class
     * @param dataProcessor 树形数据包装器
     * @param <T>           目标数据的类型
     * @param <R>           原始数据的类型
     * @return 目标数据类型的树状数据
     */
    public static <T, R> List<T> parseToTree(List<R> originData, Class<T> clazz, DataProcessor<T, R> dataProcessor, Filter<R> filter) {
        return parseToTree(originData, clazz, null, dataProcessor, filter);
    }

    /**
     * 根据数据之间的父子关系将原始数据列表转化成树型数据，并将数据转化成目标类型
     * 适用场景 每一条数据都有一个唯一标识和父数据的唯一标识
     *
     * @param originData    原始数据，列表
     * @param clazz         目标类型class
     * @param dataProcessor 树形数据包装器
     * @param <T>           目标数据的类型
     * @param <R>           原始数据的类型
     * @return 目标数据类型的树状数据
     */
    public static <T, R> List<T> parseToTree(List<R> originData, Class<T> clazz, Convert<R,T> convert, DataProcessor<T, R> dataProcessor) {
        return parseToTree(originData, clazz, convert, dataProcessor, new DefaultFilter());
    }


    /**
     * 根据数据之间的父子关系将原始数据列表转化成树型数据，并将数据转化成目标类型
     *
     * @param originData    原始数据，列表
     * @param clazz         目标类型class
     * @param convert       从原始数据转换成目标数据的数据转化器
     * @param dataProcessor 树形数据包装器
     * @param <T>           目标数据的类型
     * @param <R>           原始数据的类型
     * @return 目标数据类型的树状数据
     */
    public static <T, R> List<T> parseToTree(List<R> originData, Class<T> clazz, Convert<R, T> convert, DataProcessor<T, R> dataProcessor, Filter<R> filter) {
        //1.原始数据为空，返回一个空列表
        if (CollUtils.isEmpty(originData)) {
            return new ArrayList<>();
        }
        //2.初始化一个map用于搭建树形关系
        Map<Object, T> resultMap = new HashMap<>();
        //3.遍历数据
        originData.stream().forEach(r -> {
            if(!filter.filter(r)){
                return;
            }
            //3.1将数据转换为指定类型的数据
            T current = BeanUtils.copyBean(r, clazz, convert);
            //3.2给当前数据初始化一个空的子数据列表
            dataProcessor.setChild(current, new ArrayList<>());
            //3.3获取当前数据的唯一key
            Object key = dataProcessor.getKey(r);
            //3.4从resultMap中获取当前数据，主要把已经添加的数据的子数据copy出来
            T currentInMap = resultMap.get(key);
            if (currentInMap != null) {
                //3.5发现当前数据在resultMap中已经保存了，将它的子列表设置到新生成的目标数据中
                List<T> children = dataProcessor.getChild(currentInMap);
                dataProcessor.setChild(current, children);
            }

            //3.6获取当前数据的父数据key,如果父数据不存在初始化一个空
            Object parentKey = dataProcessor.getParentKey(r);
            T parent = resultMap.get(parentKey);
            //3.7初始化父数据
            if (parent == null) {
                parent = ReflectUtils.newInstance(clazz);
                dataProcessor.setChild(parent, new ArrayList<>());
            }
            //3.8将子数据添加到父数据的子列表中
            List<T> children = dataProcessor.getChild(parent);
            children.add(current);
            //3.9将父数据放入resultMap中
            resultMap.put(parentKey, parent);
            //3.10将子数据放入resultMap中
            resultMap.put(dataProcessor.getKey(r), current);
        });
        //4.将组装好的数据取出
        T t = resultMap.get(dataProcessor.getRootKey());
        return t== null ? null : dataProcessor.getChild(t);
    }

    /**
     * 树形数据处理器
     *
     * @param <T> 目标数据
     * @param <R> 原始数据
     */
    public interface DataProcessor<T, R> {
        /**
         * 从当前数据中获取父数据的key，也就是和父数据的关系
         *
         * @param r
         * @return
         */
        Object getParentKey(R r);

        /**
         * 获取当前数据的标识
         *
         * @param r
         * @return
         */
        Object getKey(R r);

        /**
         * 获取整个树的根数据
         *
         * @return
         */
        Object getRootKey();

        /**
         * 获取子数据列表
         *
         * @param t
         * @return
         */
        List<T> getChild(T t);

        /**
         * 将子数据列表放入到父数据中
         *
         * @param parent
         * @param child
         */
        void setChild(T parent, List<T> child);

    }

    public interface ToListDataProcessor<T, R> {
        Object getKey(R r);

        void setParent(T t, Object parentKey);

        List<R> getChildren(R r);
    }

    public interface CalculateDataProcessor<T> {

        Object getData(T t);

        List<T> getChildData(T t);

        Object calculate(Object ... datas);

        void setResult(T t, Object result);
    }

    /**
     * 过滤器
     * @param <T>
     */
    public interface Filter<T>{
        default boolean filter(T t){
            return true;
        }
    }

    public static class DefaultFilter<T> implements Filter{

    }
}