package com.tianji.common.utils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;

import java.lang.reflect.Method;

public class AspectUtils {

    /**
     * 获取被拦截方法对象
     * MethodSignature.getMethod() 获取的是顶层接口或者父类的方法对象
     * 所以应该使用反射获取当前对象的方法对象
     */
    public static Method getMethod(ProceedingJoinPoint pjp) {
        //获取参数的类型
        Signature sig = pjp.getSignature();
        if (sig instanceof MethodSignature) {
            MethodSignature methodSignature = (MethodSignature) sig;
            return methodSignature.getMethod();
        } else {
            throw new IllegalArgumentException("It's not method");
        }
    }

    /**
     * 在aop切面中SPEL表达式对formatter进行格式化，
     * 转换出指定的值
     *
     * @param formatter
     * @param method
     * @param args
     * @return
     */
    public static String parse(String formatter, Method method, Object[] args) {
        LocalVariableTableParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
        return SPELUtils.parse(formatter, nameDiscoverer.getParameterNames(method), args);
    }
}
