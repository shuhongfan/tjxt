package com.tianji.course.utils;

import com.tianji.common.utils.CollUtils;
import com.tianji.common.utils.ObjectUtils;
import com.tianji.common.utils.ReflectUtils;
import com.tianji.common.utils.StringUtils;
import com.tianji.course.domain.po.Subject;

import java.util.ArrayList;
import java.util.List;

/**
 * 题目工具类
 *
 * @ClassName SubjectUtils
 * @Author wusongsong
 * @Date 2022/7/15 17:13
 * @Version
 **/
public class SubjectUtils {

    /**
     * 将选项列表中的选项设置到题目中
     *
     * @param subject 题目
     * @param options 选项
     */
    public static void setOptions(Subject subject, List<String> options) {
        if (CollUtils.isNotEmpty(options)) {
            for (int count = 0; count < options.size(); count++) {
                ReflectUtils.setFieldValue(subject, "option" + (count + 1), options.get(count));
            }
        }
    }

    /**
     * 从题目中获取选项
     *
     * @param subject 题目
     * @return 选项
     */
    public static List<String> getOptions(Subject subject) {
        List<String> options = new ArrayList<>();
        for (int count = 1; count <= 10; count++) {
            Object option = ReflectUtils.getFieldValue(subject, "option" + count);
            if (ObjectUtils.isEmpty(option) || StringUtils.isEmpty((String)option)) {
                return options;
            }
            options.add((String) option);
        }
        return options;
    }
}
