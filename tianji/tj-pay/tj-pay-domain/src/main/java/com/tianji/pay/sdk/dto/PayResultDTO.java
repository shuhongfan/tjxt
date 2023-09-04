package com.tianji.pay.sdk.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@ApiModel(description = "支付结果")
@AllArgsConstructor
public class PayResultDTO {

    public static final int PAYING = 1;
    public static final int FAILED = 2;
    public static final int SUCCESS = 3;
    public static final String OK = "ok";

    @ApiModelProperty("支付结果，1：支付中，2：支付失败，3：支付成功")
    private final int status;
    @ApiModelProperty("支付失败原因")
    private final String msg;
    @ApiModelProperty("业务订单号")
    private final Long bizOrderId;
    @ApiModelProperty("业务订单号")
    private final Long payOrderNo;
    @ApiModelProperty("支付渠道")
    private final String payChannel;
    @ApiModelProperty("支付成功时间")
    private final LocalDateTime successTime;
}
