package com.tianji.pay.sdk.client;

import com.tianji.pay.sdk.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient("pay-service")
public interface PayClient {
    /**
     * 查询支付渠道
     * @return 支付渠道列表
     */
    @GetMapping("/pay-channels/list")
    List<PayChannelDTO> listAllPayChannels();
    /**
     * 扫码支付申请支付单，返回支付url地址，用于生产二维码
     *
     * @param payApplyDTO 申请支付单的参数信息
     * @return 支付链接，需要前端生成二维码
     */
    @PostMapping("/pay-orders")
    String applyPayOrder(@RequestBody PayApplyDTO payApplyDTO);

    /**
     * 根据业务端订单id查询支付结果
     *
     * @param bizOrderId 业务订单id
     * @return 支付结果
     */
    @GetMapping("/pay-orders/{bizOrderId}/status")
    PayResultDTO queryPayResult(@PathVariable("bizOrderId") Long bizOrderId);

    /**
     * 申请退款接口
     *
     * @param refundApplyDTO 退款参数
     * @return 退款结果
     */
    @PostMapping("/refund-orders")
    RefundResultDTO applyRefund(@RequestBody RefundApplyDTO refundApplyDTO);

    /**
     * 查询退款结果
     *
     * @param bizRefundOrderId 要退款的订单id
     * @return 退款结果
     */
    @GetMapping("/refund-orders/{bizRefundOrderId}/status")
    RefundResultDTO queryRefundResult(@PathVariable("bizRefundOrderId") Long bizRefundOrderId);
}
