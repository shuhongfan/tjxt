package com.tianji.user.constants;

public interface UserErrorInfo {
    interface Msg {
        String INVALID_UN_OR_PW = "用户登录信息错误";
        String INVALID_UN = "用户名或手机号不能为空";
        String INVALID_USER_TYPE = "无效的用户类型";
        String PHONE_NOT_EXISTS = "手机号不存在";
        String ILLEGAL_LOGIN_TYPE = "无效的登录方式";
        String USER_ID_NOT_EXISTS = "用户id不存在";

        String USER_FROZEN = "账号已冻结";
        String PHONE_ALREADY_EXISTS = "手机号已存在";
        String INVALID_USER_STATUS = "用户状态不正确";
    }
}
