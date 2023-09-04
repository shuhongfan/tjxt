package com.tianji.user.domain.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tianji.common.enums.UserType;
import com.tianji.user.enums.UserStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 学员用户表
 * </p>
 *
 * @author 虎哥
 * @since 2022-06-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 手机号
     */
    private String cellPhone;

    /**
     * 密码
     */
    private String password;

    /**
     * 账户状态：0-禁用，1-正常
     */
    private UserStatus status;

    /**
     * 用户类型：1-其他员工, 2-普通学员，3-老师
     */
    private UserType type;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    /**
     * 创建者id
     */

    private Long creater;
    /**
     * 修改者id
     */

    private Long updater;
}
