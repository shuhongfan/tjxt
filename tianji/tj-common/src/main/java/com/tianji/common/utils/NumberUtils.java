package com.tianji.common.utils;

import cn.hutool.core.util.NumberUtil;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class NumberUtils extends NumberUtil {


    /**
     * 如果number为空，将number转换为0，否则原数字返回
     *
     * @param number 原数值
     * @return 整型数字，0或原数字
     */
    public static Integer null2Zero(Integer number){
        return number == null ? 0 : number;
    }

    /**
     * 如果number为空，将number转换为0，否则原数字返回
     *
     * @param number 原数值
     * @return 整型数字，0或原数字
     */
    public static Double null2Zero(Double number){
        return number == null ? 0 : number;
    }

    /**
     * 如果number为空，将number转换为0L，否则原数字返回
     *
     * @param number  原数值
     * @return 长整型数字，0L或原数字
     */
    public static Long null2Zero(Long number){
        return number == null ? 0L : number;
    }


    public static Double setScale(Double number) {
        return new BigDecimal(number)
                .setScale(2, BigDecimal.ROUND_HALF_UP)
                .doubleValue();
    }
    /**
     * 比较两个数字是否相同，
     * @param number1 数值1
     * @param number2 数值2
     * @return 是否一致
     */
    public static boolean equals(Integer number1, Integer number2) {
        if(number1 == null || number2 == null){
            return false;
        }
        return number1.equals(number2);
    }

    /**
     * 数字除法保留指定小数位
     * @param num1 被除数
     * @param num2 除数
     * @param scale 小数点位数
     * @return 结果
     */
    public static Double divToDouble(Integer num1, Integer num2, int scale){
        if(num2 == null || num2 ==0 || num1 == null || num1 == 0) {
            return 0d;
        }
        return div(num1, num2, scale).doubleValue();
    }

    public static  Double max(List<Double> data){
        if(CollUtils.isEmpty(data)){
            return null;
        }
        return data.stream()
                .max(Comparator.comparingDouble(num -> num))
                .orElse(0d);
    }
    public static  Double min(List<Double> data){
        if(CollUtils.isEmpty(data)){
            return null;
        }
        return data.stream()
                .min(Comparator.comparingDouble(num -> num))
                .orElse(0d);
    }

    public static Double average(List<Double> data){
        if(CollUtils.isEmpty(data)){
            return 0d;
        }
        return data.stream()
                .collect(Collectors.averagingDouble(Double::doubleValue));

    }

    public static Integer toInt(Object obj) {
        return obj == null ? null
                : obj instanceof Integer
                ? (int) obj : null;
    }

    /**
     * 取绝对值，如果为null，返回0
     * @param number 数值
     * @return 绝对值
     */
    public static int abs(Integer number) {
        return number == null
                ? 0
                : Math.abs(number);
    }

    /**
     * 数字格式化字符串，不足位数补0
     *
     * @param originNumber 原始数字
     * @param digit 数字位数
     * @return 字符串
     */
    public static String  repair0(Integer originNumber, Integer digit){
        StringBuilder number = new StringBuilder(originNumber + "");
        while (number.length() < digit) {
            number.insert(0, "0");
        }
        return number.toString();
    }


    public static String scaleToStr(Integer num, int offset) {
        // 1.计算位数
        int m = (int) Math.pow(10, offset);
        // 2.计算商
        int s = num / m;
        // 3.计算余数
        int y = num % m;
        if (y == 0) {
            return Integer.toString(s);
        }
        // 2.计算余数
        return s + "." + y;
    }
}
