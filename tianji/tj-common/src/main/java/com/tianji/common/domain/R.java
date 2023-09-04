package com.tianji.common.domain;

import com.tianji.common.constants.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.slf4j.MDC;

import static com.tianji.common.constants.ErrorInfo.Code.FAILED;
import static com.tianji.common.constants.ErrorInfo.Code.SUCCESS;
import static com.tianji.common.constants.ErrorInfo.Msg.OK;

@Data
@ApiModel(description = "通用响应结果")
public class R<T> {
    @ApiModelProperty(value = "业务状态码，200-成功，其它-失败")
    private int code;
    @ApiModelProperty(value = "响应消息", example = "OK")
    private String msg;
    @ApiModelProperty(value = "响应数据")
    private T data;
    @ApiModelProperty(value = "请求id", example = "1af123c11412e")
    private String requestId;

    public static R<Void> ok() {
        return new R<Void>(SUCCESS, OK, null);
    }

    public static <T> R<T> ok(T data) {
        return new R<>(SUCCESS, OK, data);
    }

    public static <T> R<T> error(String msg) {
        return new R<>(FAILED, msg, null);
    }

    public static <T> R<T> error(int code, String msg) {
        return new R<>(code, msg, null);
    }

    public R() {
    }

    public R(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.requestId = MDC.get(Constant.REQUEST_ID_HEADER);
    }

    public boolean success(){
        return code == SUCCESS;
    }

    public R<T> requestId(String requestId) {
        this.requestId = requestId;
        return this;
    }
}
