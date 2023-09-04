package com.tianji.auth.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 登录信息记录表
 * </p>
 *
 * @author 虎哥
 * @since 2022-07-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("login_record")
public class LoginRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户id
     */
    private String cellPhone;

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
