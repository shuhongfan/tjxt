package com.tianji.pay.controller;


import com.tianji.pay.sdk.dto.RefundApplyDTO;
import com.tianji.pay.sdk.dto.RefundResultDTO;
import com.tianji.pay.service.IRefundOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 退款订单 前端控制器
 * </p>
 *
 * @author 虎哥
 * @since 2022-08-26
 */
@RestController
@RequestMapping("/refund-orders")
@RequiredArgsConstructor
@Api(tags = "支付相关接口")
public class RefundOrderController {

    private final IRefundOrderService refundOrderService;

    @PostMapping
    @ApiOperation("申请退款接口")
    public RefundResultDTO applyRefund(@RequestBody RefundApplyDTO refundApplyDTO) {
        return refundOrderService.applyRefund(refundApplyDTO);
    }

    @GetMapping("{bizRefundOrderId}/status")
    @ApiOperation("查询退款结果")
    public RefundResultDTO queryRefundResult(
            @ApiParam("业务端退款的子订单id") @PathVariable("bizRefundOrderId") Long bizRefundOrderId) {
        return refundOrderService.queryRefundResult(bizRefundOrderId);
    }

/*    @GetMapping("{bizRefundOrderId}/detail")
    @ApiOperation("查询退款详情")
    public RefundResultDTO queryRefundDetail(
            @ApiParam("业务端退款的子订单id") @PathVariable("bizRefundOrderId") Long bizRefundOrderId) {
        return refundOrderService.queryRefundDetail(bizRefundOrderId);
    }*/
}
