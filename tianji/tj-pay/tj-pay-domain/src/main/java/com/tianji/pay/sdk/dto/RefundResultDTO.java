package com.tianji.pay.sdk.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@ApiModel(description = "支付结果")
@AllArgsConstructor
@Builder
public class RefundResultDTO {

    public static final int RUNNING = 1;
    public static final int FAILED = 2;
    public static final int SUCCESS = 3;
    public static final String OK = "ok";

    @ApiModelProperty("退款状态，1：退款中，2：退款失败，3：退款成功")
    private int status;
    @ApiModelProperty("支付失败原因")
    private String msg;
    @ApiModelProperty("业务端支付订单号")
    private Long bizPayOrderId;
    @ApiModelProperty("业务端退款订单号")
    private Long bizRefundOrderId;
    @ApiModelProperty("支付流水交易单号")
    private Long payOrderNo;
    @ApiModelProperty("退款交易单号")
    private Long refundOrderNo;
    @ApiModelProperty("支付渠道")
    private String payChannel;
    @ApiModelProperty("退款渠道")
    private String refundChannel;

    public static RefundResultDTOBuilder success() {
        return builder().status(SUCCESS).msg(OK);
    }

    public static RefundResultDTOBuilder running() {
        return builder().status(RUNNING);
    }

    public static RefundResultDTOBuilder failed() {
        return builder().status(FAILED);
    }
}
