package com.tianji.auth.common.constants;

public interface AuthErrorInfo {

    interface Msg {
        String INVALID_STAFF_TYPE = "无效的账户类型";


        String INVALID_ROLE_ID = "绑定的角色不存在";
        String PRIVILEGE_EXISTS = "权限信息已存在";
        String PRIVILEGE_NOT_FOUND = "权限数据不存在";
        String ROLE_NOT_FOUND = "角色数据不存在";
        String MENU_NOT_FOUND = "菜单数据不存在";
        String UNAUTHORIZED = "未登录";
        String FORBIDDEN = "无访问权限";
        String INVALID_TOKEN = "无效的token";
        String EXPIRED_TOKEN = "token已过期";
        String INVALID_TOKEN_PAYLOAD = "token参数格式错误";
    }

    interface Code {
        // 过期token
        int EXPIRED_TOKEN_CODE = 40101;
        // 无效token
        int INVALID_TOKEN_CODE = 40102;
    }
}