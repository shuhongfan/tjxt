package com.tianji.api.dto.user;

import com.tianji.common.constants.RegexConstants;
import com.tianji.common.validate.annotations.EnumValid;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@ApiModel(description = "用户详情")
public class UserDTO {
    @ApiModelProperty(value = "用户id", example = "1")
    private Long id;
    @ApiModelProperty(value = "手机", example = "13890011009")
    @Pattern(regexp = RegexConstants.PHONE_PATTERN, message = "手机号格式错误")
    private String cellPhone;
    @ApiModelProperty(value = "用户名称/昵称", example = "李四")
    private String name;
    @ApiModelProperty(value = "用户类型，1-其他员工,2-普通学员，3-老师", example = "2")
    @EnumValid(enumeration = {1,2,3}, message = "用户类型错误")
    @NotNull
    private Integer type;
    @ApiModelProperty(value = "角色id，老师和学生不用填", example = "5")
    private Long roleId;
    @ApiModelProperty(value = "头像", example = "default-user-icon.jpg")
    private String icon;
    @ApiModelProperty(value = "岗位", example = "讲师")
    private String job;
    @ApiModelProperty(value = "个人介绍", example = "黑马高级Java讲师")
    private String intro;
    @ApiModelProperty(value = "形象照地址", example = "default-teacher-photo.jpg")
    private String photo;
    @ApiModelProperty(value = "用户名", example = "13800010004")
    private String username;
    @ApiModelProperty(value = "邮箱")
    @Email
    private String email;
    @ApiModelProperty(value = "QQ号码")
    private String qq;
    @ApiModelProperty(value = "省")
    private String province;
    @ApiModelProperty(value = "市")
    private String city;
    @ApiModelProperty(value = "区")
    private String district;
    @ApiModelProperty(value = "性别：0-男性，1-女性", example = "0")
    @EnumValid(enumeration = {0, 1}, message = "性别格式不正确")
    private Integer gender;
}
