package com.tianji.trade.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiModel(description = "订单详细信息")
public class OrderVO {
    @ApiModelProperty("订单id")
    private Long id;
    @ApiModelProperty("订单创建时间")
    private LocalDateTime createTime;
    @ApiModelProperty("订单实付金额")
    private Integer realAmount;
    @ApiModelProperty("订单明细金额")
    private Integer totalAmount;
    @ApiModelProperty("优惠券规则,可以有多个优惠券规则")
    private String couponDesc;
    @ApiModelProperty("优惠总金额")
    private Double discountAmount;
    @ApiModelProperty("订单状态，1：待支付，2：已支付，3：已关闭，4：已完成，5：已报名，6：已申请退款")
    private Integer status;
    @ApiModelProperty("订单状态描述")
    private String statusDesc;
    @ApiModelProperty("订单状态描述")
    private String message;
    @ApiModelProperty("订单进度明细")
    private List<OrderProgressNodeVO> progressNodes;

    @ApiModelProperty("订单中课程明细")
    private List<OrderDetailVO> details;
}
