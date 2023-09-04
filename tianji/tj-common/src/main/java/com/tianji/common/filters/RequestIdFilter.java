package com.tianji.common.filters;


import com.tianji.common.constants.Constant;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Order(Ordered.HIGHEST_PRECEDENCE)
@WebFilter(filterName = "requestIdFilter", urlPatterns = "/**")
public class RequestIdFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // 1.获取request
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        // 2.获取请求头中的requestId
        String requestId = request.getHeader(Constant.REQUEST_ID_HEADER);
        try {
            // 3.存入MDC
            MDC.put(Constant.REQUEST_ID_HEADER, requestId);
            filterChain.doFilter(request, servletResponse);
        }finally {
            // 4.移除
            MDC.clear();
        }
    }
}
