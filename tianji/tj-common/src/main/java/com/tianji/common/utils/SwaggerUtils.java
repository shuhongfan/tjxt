package com.tianji.common.utils;

import io.swagger.models.Swagger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import springfox.documentation.swagger2.web.SwaggerTransformationContext;

import java.lang.reflect.Constructor;

/**
 * 处理swagger的工具
 **/
@Slf4j
public class SwaggerUtils {


    /**
     * 通过反射获取创建SwaggerTransformationContext对象
     */
    public static SwaggerTransformationContext getInstance(Swagger swagger, ServerHttpRequest request){

        Constructor<SwaggerTransformationContext> constructor =
                ReflectUtils.getConstructor(SwaggerTransformationContext.class);
        try {
            constructor.setAccessible(true);
            return constructor.newInstance(swagger, request);
        }catch (Exception e){
            log.error("生成swagger transformation 失败 e:",e);
        }
        return null;
    }
}
