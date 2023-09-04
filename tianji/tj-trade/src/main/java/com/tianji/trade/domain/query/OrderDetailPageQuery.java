package com.tianji.trade.domain.query;

import com.tianji.common.domain.query.PageQuery;
import com.tianji.common.utils.DateUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(description = "订单明细查询条件")
public class OrderDetailPageQuery extends PageQuery {
    @ApiModelProperty("订单明细id")
    private Long id;
    @ApiModelProperty("订单状态：1：待支付，2：已支付，3：已关闭，4：已完成，5：已报名")
    private Integer status;
    @ApiModelProperty("退款状态：1：待审批，2：取消退款，3：同意退款，4：拒绝退款，5：退款成功，6：退款失败")
    private Integer refundStatus;
    @ApiModelProperty("支付方式:wxPay:微信，aliPay：支付宝")
    private String payChannel;
    @ApiModelProperty("手机号")
    private String mobile;
    @ApiModelProperty("下单开始时间")
    @DateTimeFormat(pattern = DateUtils.DEFAULT_DATE_TIME_FORMAT)
    private LocalDateTime orderStartTime;
    @ApiModelProperty("下单结束时间")
    @DateTimeFormat(pattern = DateUtils.DEFAULT_DATE_TIME_FORMAT)
    private LocalDateTime orderEndTime;
}
