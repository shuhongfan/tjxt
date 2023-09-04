package com.tianji.common.constants;

import cn.hutool.core.lang.RegexPool;

public interface RegexConstants extends RegexPool {
    /**
     * 手机号正则
     */
    String PHONE_PATTERN = "^1([38][0-9]|4[579]|5[0-3,5-9]|6[6]|7[0135678]|9[89])\\d{8}$";
    /**
     * 邮箱正则
     */
    String EMAIL_PATTERN = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
    /**
     * 密码正则。6~32位的字母、数字、下划线
     */
    String PASSWORD_PATTERN = "^\\w{4,24}$";
    /**
     * 用户名正则。6~32位的字母、数字、下划线
     */
    String USERNAME_PATTERN = "^\\w{4,32}$";
    /**
     * 验证码正则, 6位数字或字母
     */
    String VERIFY_CODE_PATTERN = "^[a-zA-Z\\d]{6}$";

    /**
     * 优惠券兑换码模板
     */
    String COUPON_CODE_PATTERN = "^[23456789ABCDEFGHJKLMNPQRSTUVWXYZ]{8,10}$";
}
