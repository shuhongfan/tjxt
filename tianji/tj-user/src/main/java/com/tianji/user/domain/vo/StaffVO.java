package com.tianji.user.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel(description = "员工用户")
public class StaffVO {
    @ApiModelProperty(value = "主键", example = "1")
    private Long id;
    @ApiModelProperty(value = "头像", example = "default-user-icon.jpg")
    private String icon;
    @ApiModelProperty(value = "手机号", example = "13800010002")
    private String cellPhone;
    @ApiModelProperty(value = "员工姓名", example = "user_138foo0002")
    private String name;
    @ApiModelProperty(value = "角色id", example = "5")
    private Long roleId;
    @ApiModelProperty(value = "角色名称", example = "5")
    private String roleName;
    @ApiModelProperty(value = "注册时间", example = "2022-07-22")
    private LocalDateTime createTime;
    @ApiModelProperty(value = "账号状态", example = "0")
    private Integer status;
}
