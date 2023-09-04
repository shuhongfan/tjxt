package com.tianji.promotion.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 基于回溯算法的全排列工具类
 */
public class PermuteUtil {
    /**
     * 将[0~n)的所有数字重组，生成不重复的所有排列方案
     *
     * @param n 数字n
     * @return 排列组合
     */
    public static List<List<Byte>> permute(int n) {
        List<List<Byte>> res = new ArrayList<>();

        List<Byte> input = new ArrayList<>(n);
        for (byte i = 0; i < n; i++) {
            input.add(i);
        }

        backtrack(n, input, res, 0);
        return res;
    }

    /**
     * 将指定集合中的元素重组，生成所有的排列组合方案
     *
     * @param input 输入的集合
     * @param <T>   集合类型
     * @return 重组后的集合方案
     */
    public static <T> List<List<T>> permute(List<T> input) {
        List<List<T>> res = new ArrayList<>();
        backtrack(input.size(), input, res, 0);
        return res;
    }

    private static <T> void backtrack(int n, List<T> input, List<List<T>> res, int first) {
        // 所有数都填完了
        if (first == n) {
            res.add(new ArrayList<>(input));
        }
        for (int i = first; i < n; i++) {
            // 动态维护数组
            Collections.swap(input, first, i);
            // 继续递归填下一个数
            backtrack(n, input, res, first + 1);
            // 撤销操作
            Collections.swap(input, first, i);
        }
    }
}