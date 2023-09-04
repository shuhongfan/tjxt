package com.tianji.common.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;

@ApiModel(description = "DTO基础属性")
public class BaseDTO {
    @ApiModelProperty("创建人id")
    private Long creater;
    @ApiModelProperty("更新人id")
    private Long updater;
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
}
