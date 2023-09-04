package com.tianji.common.autoconfigure.mvc;


import com.tianji.common.autoconfigure.mvc.advice.CommonExceptionAdvice;
import com.tianji.common.autoconfigure.mvc.advice.WrapperResponseBodyAdvice;
import com.tianji.common.autoconfigure.mvc.converter.WrapperResponseMessageConverter;
import com.tianji.common.filters.RequestIdFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;

@ConditionalOnClass({CommonExceptionAdvice.class, Filter.class})
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    /**
     * <h1>通用的ControllerAdvice异常处理器</h1>
     */
    @Bean
    public CommonExceptionAdvice commonExceptionAdvice(){
        return new CommonExceptionAdvice();
    }

    @Bean
    public RequestIdFilter requestIdFilter(){
        return new RequestIdFilter();
    }

    @Bean
    @ConditionalOnMissingClass("org.springframework.cloud.gateway.filter.GlobalFilter")
    public WrapperResponseMessageConverter wrapperResponseMessageConverter(
            MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter
    ){
        return new WrapperResponseMessageConverter(mappingJackson2HttpMessageConverter);
    }

    @Bean
    public WrapperResponseBodyAdvice wrapperResponseBodyAdvice(){
        return new WrapperResponseBodyAdvice();
    }
}
