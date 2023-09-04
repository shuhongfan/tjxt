package com.tianji.trade.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(description = "课程加入购物车")
public class CartsAddDTO {
    @ApiModelProperty("要加入购物车的课程id")
    @NotNull(message = "课程id不能为空")
    private Long courseId;
}