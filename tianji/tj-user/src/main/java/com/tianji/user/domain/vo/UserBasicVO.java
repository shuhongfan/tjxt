package com.tianji.user.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "用户信息")
public class UserBasicVO {
    @ApiModelProperty(value = "用户id", example = "1")
    private Long id;
    @ApiModelProperty(value = "用户名称/昵称", example = "李四")
    private String name;
    @ApiModelProperty(value = "用户类型,1-员工,2-普通学员，3-老师", example = "2")
    private Integer type;
    @ApiModelProperty(value = "头像", example = "default-user-icon.jpg")
    private String icon;
}
