package com.tianji.pay.sdk.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@Builder
@ApiModel(description = "支付申请参数")
public class PayApplyDTO {
    @ApiModelProperty("业务订单号")
    @NotNull(message = "业务订单id不能为空")
    private Long bizOrderNo;
    @ApiModelProperty("下单用户id")
    @NotNull(message = "下单用户id不能为空")
    private Long bizUserId;
    @Min(value = 1, message = "支付金额必须为正数")
    @ApiModelProperty("支付金额，以分为单位")
    private Integer amount;
    @NotNull(message = "支付渠道编码不能为空")
    @ApiModelProperty("支付渠道编码，例如：aliPay")
    private String payChannelCode;
    @ApiModelProperty("支付方式: 1-h5；2-小程序；3-公众号；4-扫码")
    @NotNull(message = "支付方式不能为空")
    private Integer payType;
    @ApiModelProperty("订单中的商品信息")
    @NotNull(message = "订单中的商品信息不能为空")
    private String orderInfo;
}
