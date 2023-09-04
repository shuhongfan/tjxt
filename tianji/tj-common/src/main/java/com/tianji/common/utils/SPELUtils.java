package com.tianji.common.utils;

import lombok.Data;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SPELUtils {


    /**
     * 将模板中的表达式替换成args参数中的值
     *
     * @param formatter   模板
     * @param paraNameArr 方法对应的参数名称
     * @param args        方法参数值value，用来进行退换对应的表达式
     * @return 模板替换后的字符串
     *
     * 例    format : tj:#{user.id}
     *      paraNameAddr [user]
     *      args [{"user":{"id":1}}]
     *
     *      转换后结果 -> tj:1
     */
    public static String parse(String formatter, String[] paraNameArr, Object[] args) {
        if (StringUtils.isNotBlank(formatter) && formatter.indexOf("#") > -1) {
            //正则表达式 #{user.id},
            Pattern pattern = Pattern.compile("(\\#\\{([^\\}]*)\\})");
            Matcher matcher = pattern.matcher(formatter);
            //将正则表达式中#{}的值取出放在keys中
            List<String> keys = new ArrayList<>();
            while (matcher.find()) {
                keys.add(matcher.group());
            }
            if (!CollUtils.isEmpty(keys)) {
                //SPEL表达式对象
                ExpressionParser parser = new SpelExpressionParser();
                StandardEvaluationContext context = new StandardEvaluationContext();
                //将名称和value一一对应
                for (int i = 0; i < paraNameArr.length; i++) {
                    context.setVariable(paraNameArr[i], args[i]);
                }

                for (String tmp : keys) {
                    formatter = formatter.replace(tmp,
                            //通过SPEL表达式获取对应的值，然后再替换掉原有值
                            parser.parseExpression("#" + tmp.substring(2, tmp.length() - 1)).getValue(context, String.class));
                }
                return formatter;
            }
        }
        return null;
    }

    @Data
    public static class User {
        private Long id = 1L;
    }
    public static void main(String[] args) {

        Object [] users = new Object[1];
        users[0] = new User();

        System.out.println(parse("tj:#{user.id}", new String[]{"user"}, users));
    }
}
