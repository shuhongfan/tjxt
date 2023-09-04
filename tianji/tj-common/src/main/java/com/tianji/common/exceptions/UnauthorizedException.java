package com.tianji.common.exceptions;

import lombok.Getter;

@Getter
public class UnauthorizedException extends CommonException{
    private final int status = 401;

    public UnauthorizedException(String message) {
        super(401, message);
    }

    public UnauthorizedException(int code, String message) {
        super(code, message);
    }

    public UnauthorizedException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }
}
