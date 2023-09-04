package com.tianji.pay.sdk.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(description = "退款请求参数")
public class RefundApplyDTO {

    @ApiModelProperty("支付时传入的业务订单id")
    private Long bizOrderNo;
    @ApiModelProperty("本次要退款的业务订单id，因为有拆单，这里是子订单id")
    private Long bizRefundOrderNo;
    @ApiModelProperty("子订单的退款金额,单位为分")
    private Integer refundAmount;
}