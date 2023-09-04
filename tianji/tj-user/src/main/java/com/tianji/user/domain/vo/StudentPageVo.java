package com.tianji.user.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel(description = "学生信息")
public class StudentPageVo {
    @ApiModelProperty(value = "学生id，也是用户id", example = "1")
    private Long id;
    @ApiModelProperty(value = "学生名称", example = "张三")
    private String name;
    @ApiModelProperty(value = "头像", example = "default-icon.jpg")
    private String icon;
    @ApiModelProperty(value = "手机号", example = "13800010004")
    private String cellPhone;
    @ApiModelProperty(value = "性别：0-男性，1-女性", example = "0")
    private Integer gender;
    @ApiModelProperty(value = "购买/报名课程数量", example = "12")
    private Integer courseAmount;
    @ApiModelProperty(value = "注册时间", example = "2022-07-12")
    private LocalDateTime createTime;
    @ApiModelProperty(value = "账户状态，0-禁用，1-正常", example = "1")
    private Integer status;
}