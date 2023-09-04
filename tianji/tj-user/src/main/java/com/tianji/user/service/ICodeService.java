package com.tianji.user.service;

public interface ICodeService {
    void sendVerifyCode(String phone);
    void verifyCode(String phone, String code);
}
