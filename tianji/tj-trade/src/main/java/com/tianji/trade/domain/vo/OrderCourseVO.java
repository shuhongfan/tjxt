package com.tianji.trade.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "订单中课程信息")
public class OrderCourseVO {
    @ApiModelProperty("课程id")
    private Long id;
    @ApiModelProperty("课程名称")
    private String name;
    @ApiModelProperty("课程封面url")
    private String coverUrl;
    @ApiModelProperty("课程价格，单位元")
    private Integer price;
}
