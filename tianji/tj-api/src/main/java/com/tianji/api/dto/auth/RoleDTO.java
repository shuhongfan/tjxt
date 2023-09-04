package com.tianji.api.dto.auth;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 角色表
 * </p>
 *
 * @author 虎哥
 * @since 2022-06-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(description = "角色实体")
public class RoleDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键", example = "1")
    private Long id;

    /**
     * 角色代号，例如：admin
     */
    @ApiModelProperty(value = "角色代号", example = "admin")
    private String code;

    /**
     * 角色描述
     */
    @ApiModelProperty(value = "角色名称", example = "教师")
    private String name;
}
