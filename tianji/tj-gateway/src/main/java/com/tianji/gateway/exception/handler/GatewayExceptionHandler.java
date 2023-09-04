package com.tianji.gateway.exception.handler;

import com.tianji.common.constants.Constant;
import com.tianji.common.domain.R;
import com.tianji.common.exceptions.CommonException;
import com.tianji.common.exceptions.UnauthorizedException;
import com.tianji.common.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.tianji.common.constants.ErrorInfo.Code.FAILED;
import static com.tianji.common.constants.ErrorInfo.Msg.SERVER_INTER_ERROR;

@Slf4j
@Component
public class GatewayExceptionHandler implements ErrorWebExceptionHandler, Ordered {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        // 1.获取响应
        ServerHttpResponse response = exchange.getResponse();
        // 2.判断是否已处理
        if (response.isCommitted()) {
            // 如果已经提交，直接结束，避免重复处理
            return Mono.error(ex);
        }

        // 3.按照异常类型进行翻译处理，翻译的结果易于前端理解
        String message;
        int code = FAILED;
        if (ex instanceof UnauthorizedException) {
            // 登录异常，直接返回状态码
            UnauthorizedException e = (UnauthorizedException) ex;
            return Mono.error(new ResponseStatusException(e.getStatus(), e.getMessage(), e));
        } else if (ex instanceof CommonException) {
            CommonException e = (CommonException) ex;
            code = e.getCode();
            message = e.getMessage();
        } else if (ex instanceof NotFoundException) {
            message = "服务不存在";
        } else if (ex instanceof ResponseStatusException) {
            message = ex.getMessage();
        } else {
            message = SERVER_INTER_ERROR;
            // 4.记录日志
            writeLog(exchange, ex);
        }
        // 5.设置响应结果为 JSON
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        // 6.封装响应结果并写出
        R<Object> r = R.error(code, message);
        List<String> requestIds = response.getHeaders().get(Constant.REQUEST_ID_HEADER);
        if (requestIds != null) {
            r.requestId(requestIds.get(0));
        }
        byte[] resp = JsonUtils.toJsonStr(r).getBytes(StandardCharsets.UTF_8);
        return response.writeWith(
                Mono.fromSupplier(
                        () -> response.bufferFactory().wrap(resp)
                ));
    }

    private void writeLog(ServerWebExchange exchange, Throwable ex) {
        ServerHttpRequest request = exchange.getRequest();
        URI uri = request.getURI();
        String host = uri.getHost();
        int port = uri.getPort();
        log.error("网关路由异常-host:{} ,port:{}，uri:{},  errormessage:",
                host, port, request.getPath(), ex);
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }
}