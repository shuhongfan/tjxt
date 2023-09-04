package com.tianji.trade.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "订单条目中的课程信息")
public class OrderDetailVO {
    @ApiModelProperty("订单条目id")
    private Long id;
    @ApiModelProperty("总订单id")
    private Long orderId;
    @ApiModelProperty("课程id")
    private Long courseId;
    @ApiModelProperty("课程名称")
    private String name;
    @ApiModelProperty("封面")
    private String coverUrl;
    @ApiModelProperty("课程价格")
    private Integer price;
    @ApiModelProperty("实付金额")
    private Integer realPayAmount;
    @ApiModelProperty("退款状态")
    private Integer refundStatus;
    @ApiModelProperty("优惠券规则")
    private String couponDesc;
    @ApiModelProperty("是否可以退款")
    private Boolean canRefund;
}