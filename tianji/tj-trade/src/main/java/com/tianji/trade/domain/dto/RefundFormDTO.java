package com.tianji.trade.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(description = "退款申请数据")
public class RefundFormDTO {
    @ApiModelProperty("订单明细id")
    @NotNull(message = "请选中退款订单")
    private Long orderDetailId;
    @ApiModelProperty("退款原因")
    @NotNull(message = "请选择退款原因")
    private String refundReason;
    @ApiModelProperty("问题说明")
    @NotNull(message = "问题说明不能为空")
    private String questionDesc;
}
