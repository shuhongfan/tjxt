package com.tianji.trade.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel(description = "订单明细分页结果")
public class OrderDetailPageVO {
    @ApiModelProperty("订单明细id")
    private Long id;
    @ApiModelProperty("订单id")
    private Long orderId;
    @ApiModelProperty("学员姓名")
    private String name;
    @ApiModelProperty("手机号")
    private String mobile;
    @ApiModelProperty("订单金额，也就是课程原价")
    private Integer price;
    @ApiModelProperty("实付金额")
    private Integer realPayAmount;
    @ApiModelProperty("订单状态1：待支付，2：已支付，3：已关闭，4：已完成，5：已报名")
    private Integer status;
    @ApiModelProperty("订单状态描述")
    private String statusDesc;
    @ApiModelProperty("退款状态1：待支付，2：已支付，3：已关闭，4：已完成，5：已报名，0：表示没有退款状态")
    private Integer refundStatus;
    @ApiModelProperty("退款状态描述")
    private String refundStatusDesc;
    @ApiModelProperty("订单时间")
    private LocalDateTime createTime;
    @ApiModelProperty("支付方式")
    private String payChannel;
}