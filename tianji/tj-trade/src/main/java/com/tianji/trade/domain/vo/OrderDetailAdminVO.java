package com.tianji.trade.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiModel(description = "管理端订单条目详细信息")
public class OrderDetailAdminVO {
    @ApiModelProperty("订单条目id")
    private Long id;
    @ApiModelProperty("订单id")
    private Long orderId;
    @ApiModelProperty("退款id")
    private Long refundApplyId;

    @ApiModelProperty("支付流水单号")
    private Long payOrderNo;
    @ApiModelProperty("退款流水单号")
    private Long refundOrderNo;

    @ApiModelProperty("学员昵称")
    private String studentName;
    @ApiModelProperty("手机号")
    private String mobile;
    @ApiModelProperty("退款申请人，格式：角色-名字")
    private String refundProposerName;

    @ApiModelProperty("支付方式")
    private String payChannel;
    @ApiModelProperty("退款方式")
    private String refundChannel;
    @ApiModelProperty("退款失败原因")
    private String failedReason;

    @ApiModelProperty("申请退款原因")
    private String refundReason;
    @ApiModelProperty("申请退款描述")
    private String refundMessage;
    @ApiModelProperty("审批意见")
    private String remark;

    @ApiModelProperty("订单状态，1：待支付，2：已支付，3：已关闭，4：已完成，5：已报名")
    private Integer status;
    @ApiModelProperty("退款状态，1：待审批，2：取消退款，3：同意退款，4：拒绝退款，5：退款成功，6：退款失败")
    private Integer refundStatus;
    @ApiModelProperty("状态描述")
    private String message;

    @ApiModelProperty("订单节点进度节点列表")
    private List<OrderProgressNodeVO> nodes;

    @ApiModelProperty("课程名称")
    private String name;
    @ApiModelProperty("课程价格")
    private Integer price;
    @ApiModelProperty("实付金额")
    private Integer realPayAmount;
    @ApiModelProperty("优惠券规则")
    private String couponDesc;
    @ApiModelProperty("优惠总金额")
    private Integer discountAmount;
    @ApiModelProperty("学习有效期")
    private LocalDateTime studyValidTime;

    @ApiModelProperty("是否可以退款")
    private Boolean canRefund;
}