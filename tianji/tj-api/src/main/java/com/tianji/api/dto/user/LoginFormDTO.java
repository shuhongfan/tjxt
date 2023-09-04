package com.tianji.api.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(description = "登录表单实体")
public class LoginFormDTO {
    @ApiModelProperty(value = "登录方式：1-密码登录; 2-验证码登录", example = "1", required = true)
    @NotNull
    private Integer type;
    @ApiModelProperty(value = "用户名", example = "jack")
    private String username;
    @ApiModelProperty(value = "手机号", example = "13800010001")
    private String cellPhone;
    @ApiModelProperty(value = "密码", example = "123", required = true)
    @NotNull
    private String password;
    @ApiModelProperty(value = "7天免密登录", example = "true")
    private Boolean rememberMe = false;
}
