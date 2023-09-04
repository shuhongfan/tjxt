package com.tianji.trade.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel(description = "退款信息")
public class RefundApplyPageVO {
    @ApiModelProperty("退款id")
    private Long id;
    @ApiModelProperty("订单明细id")
    private Long orderDetailId;
    @ApiModelProperty("订单id")
    private Long orderId;
    @ApiModelProperty("退款金额")
    private Integer refundAmount;
    @ApiModelProperty("申请人")
    private String proposerName;
    @ApiModelProperty("申请人手机号")
    private String proposerMobile;
    @ApiModelProperty("退款申请状态")
    private Integer status;
    @ApiModelProperty("退款申请状态描述")
    private String refundStatusDesc;
    @ApiModelProperty("退款申请时间")
    private LocalDateTime createTime;

    @ApiModelProperty("审批人")
    private String approverName;
    @ApiModelProperty("审批时间")
    private String approveTime;

    @ApiModelProperty("退款成功时间")
    private LocalDateTime refundSuccessTime;
}