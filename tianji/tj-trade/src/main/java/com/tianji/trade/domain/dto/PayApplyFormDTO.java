package com.tianji.trade.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(description = "支付申请信息")
@Data
public class PayApplyFormDTO {
    @ApiModelProperty(value = "订单id",required = true)
    private Long orderId;
    @ApiModelProperty(value = "支付渠道码，wxPay,aliPay",required = true)
    private String payChannelCode;
}