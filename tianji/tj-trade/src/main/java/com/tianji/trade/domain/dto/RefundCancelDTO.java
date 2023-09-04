package com.tianji.trade.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(description = "退款取消")
@Data
public class RefundCancelDTO {
    @ApiModelProperty("退款申请id，订单明细id和退款申请id二选一")
    private Long id;
    @ApiModelProperty("订单明细id，订单明细id和退款申请id二选一")
    private Long orderDetailId;
}