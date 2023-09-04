package com.tianji.trade.domain.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiModel(description = "订单分页信息")
public class OrderPageVO {
    @ApiModelProperty("订单id")
    private Long id;
    @ApiModelProperty("订单创建时间")
    private LocalDateTime createTime;
    @ApiModelProperty("订单实付金额")
    private Integer realAmount;
    @ApiModelProperty("订单明细金额")
    private Integer totalAmount;
    @ApiModelProperty("订单状态1：待支付，2：已支付，3：已关闭，4：已完成，5：已报名")
    private Integer status;
    @ApiModelProperty("订单状态描述")
    private String statusDesc;
    @ApiModelProperty("订单中课程明细")
    private List<OrderDetailVO> details;
}
