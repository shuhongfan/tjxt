package com.tianji.auth.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Data
@ApiModel(description = "API权限")
public class PrivilegeDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "权限id", example = "1")
    private Long id;
    @ApiModelProperty(value = "权限所属菜单id", example = "1")
    private Long menuId;
    @ApiModelProperty(value = "权限说明", example = "新增员工")
    @NotNull(message = "权限说明不能为空")
    private String intro;
    @ApiModelProperty(value = "API请求方式", example = "GET")
    @Pattern(regexp = "^GET|POST|PUT|DELETE$", message = "请求方式必须是大写")
    private String method;
    @ApiModelProperty(value = "API请求路径", example = "/account/staff")
    @NotNull(message = "uri不能为空")
    private String uri;
    @ApiModelProperty("是否是内部权限")
    private Boolean internal;
}
