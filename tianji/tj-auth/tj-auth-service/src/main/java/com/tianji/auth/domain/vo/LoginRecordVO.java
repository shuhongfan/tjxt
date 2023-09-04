package com.tianji.auth.domain.vo;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class LoginRecordVO {
    /**
     * 用户id
     */
    private Long userId;

    /**
     * 登录时间
     */
    private LocalDateTime loginTime;

    /**
     * 登出时间
     */
    private LocalDateTime logoutTime;

    /**
     * 登录日期
     */
    private LocalDate loginDate;

    /**
     * 登录时长，单位是秒
     */
    private Long duration;

    /**
     * ip地址
     */
    private String ipv4;
}
