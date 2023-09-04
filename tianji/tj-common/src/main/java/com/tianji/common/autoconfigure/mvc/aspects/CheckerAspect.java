package com.tianji.common.autoconfigure.mvc.aspects;

import com.tianji.common.utils.ArrayUtils;
import com.tianji.common.utils.CollUtils;
import com.tianji.common.validate.Checker;
import com.tianji.common.validate.annotations.ParamChecker;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import java.util.List;

@Aspect
@Slf4j
@SuppressWarnings("all")
public class CheckerAspect {

    @Before("@annotation(paramChecker)")
    public void before(JoinPoint joinPoint, ParamChecker paramChecker) {
        Object[] args = joinPoint.getArgs();
        if(ArrayUtils.isNotEmpty(args)){
            //遍历方法参数，参数是否实现了Checker接口
            for (Object arg : args){
                if(arg instanceof Checker) {
                    //调用check方法，校验业务逻辑
                    ((Checker)arg).check();
                }else if(arg instanceof List){
                    //如果参数是一个集合也要校验
                    CollUtils.check((List) arg);
                }
            }
        }
    }
}
