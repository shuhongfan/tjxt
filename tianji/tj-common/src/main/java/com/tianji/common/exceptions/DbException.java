package com.tianji.common.exceptions;

/**
 * 数据库异常
 **/
public class DbException extends CommonException{
    public DbException(String message) {
        super(message);
    }

    public DbException(int code, String message) {
        super(code, message);
    }

    public DbException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }
}
