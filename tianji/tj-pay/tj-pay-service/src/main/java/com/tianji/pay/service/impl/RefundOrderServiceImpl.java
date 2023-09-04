package com.tianji.pay.service.impl;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tianji.common.autoconfigure.mq.RabbitMqHelper;
import com.tianji.common.autoconfigure.redisson.annotations.Lock;
import com.tianji.common.constants.MqConstants;
import com.tianji.common.domain.dto.PageDTO;
import com.tianji.common.exceptions.BadRequestException;
import com.tianji.common.exceptions.BizIllegalException;
import com.tianji.common.utils.BeanUtils;
import com.tianji.pay.domain.po.PayOrder;
import com.tianji.pay.domain.po.RefundOrder;
import com.tianji.pay.mapper.RefundOrderMapper;
import com.tianji.pay.sdk.constants.PayConstants;
import com.tianji.pay.sdk.constants.PayErrorInfo;
import com.tianji.pay.sdk.dto.RefundApplyDTO;
import com.tianji.pay.sdk.dto.RefundResultDTO;
import com.tianji.pay.service.IPayOrderService;
import com.tianji.pay.service.IRefundOrderService;
import com.tianji.pay.third.IPayService;
import com.tianji.pay.third.model.RefundResponse;
import com.tianji.pay.third.model.RefundStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

import static com.tianji.pay.sdk.constants.PayErrorInfo.INVALID_PAY_CHANNEL;

/**
 * <p>
 * 退款订单 服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2022-08-26
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RefundOrderServiceImpl extends ServiceImpl<RefundOrderMapper, RefundOrder> implements IRefundOrderService {

    private final IPayOrderService payOrderService;

    private final RabbitMqHelper rabbitMqHelper;

    @Resource
    private Map<String, IPayService> payServiceChannels;

    @Override
    public RefundResultDTO applyRefund(RefundApplyDTO refundApplyDTO) {
        log.debug("准备申请退款，业务端退款订单号：{}", refundApplyDTO.getBizRefundOrderNo());
        // 1.幂等性校验
        RefundOrder refundOrder = checkIdempotent(refundApplyDTO);
        if (refundOrder == null) {
            // 为 null说明退款处理中，无需重新申请，返回false
            return RefundResultDTO.running().msg("退款执行中").build();
        }
        // 2.获取支付渠道
        IPayService payService = payServiceChannels.get(refundOrder.getPayChannelCode());
        if (payService == null) {
            log.error("退款异常，支付渠道不存在，业务单号：{}", refundApplyDTO.getBizOrderNo());
            throw new BadRequestException(INVALID_PAY_CHANNEL);
        }
        // 3.尝试退款
        RefundResponse refundResponse = payService.refundOrder(
                refundOrder.getPayOrderNo().toString(), refundOrder.getRefundOrderNo().toString(),
                refundOrder.getRefundAmount(), refundOrder.getTotalAmount());
        // 4.更新退款结果到数据库
        updateRefundStatus(refundResponse, refundOrder.getId());
        // 5.返回退款申请结果
        RefundResultDTO refundResultDTO = transferRefundResult(refundResponse);
        refundResultDTO.setBizRefundOrderId(refundOrder.getBizRefundOrderNo());
        refundResultDTO.setBizPayOrderId(refundOrder.getBizOrderNo());
        refundResultDTO.setRefundOrderNo(refundOrder.getRefundOrderNo());
        return refundResultDTO;
    }

    private void updateRefundStatus(RefundResponse refundResponse, Long id) {
        try {
            lambdaUpdate()
                    .set(refundResponse.getSuccess(), RefundOrder::getStatus, refundResponse.getStatus())
                    .set(refundResponse.getAmount() != null, RefundOrder::getRefundAmount, refundResponse.getAmount())
                    .set(refundResponse.getChannel() != null, RefundOrder::getRefundChannel, refundResponse.getChannel())
                    .set(RefundOrder::getResultCode, refundResponse.getCode() == null ? "" : refundResponse.getCode())
                    .set(RefundOrder::getResultMsg, refundResponse.getMsg() == null ? "" : refundResponse.getMsg())
                    .eq(RefundOrder::getId, id)
                    .update();
        } catch (Exception e) {
            log.error("更新退款单状态发生异常", e);
        }
    }

    @Lock(name = PayConstants.RedisKeyFormatter.REFUND_APPLY, leaseTime = 10, autoUnlock = false)
    private RefundOrder checkIdempotent(RefundApplyDTO refundApplyDTO) {
        // 1.查询出退款单对应的支付单
        PayOrder payOrder = payOrderService.queryByBizOrderNo(refundApplyDTO.getBizOrderNo());
        if (payOrder == null) {
            // 支付单为空，无法退款
            throw new BizIllegalException(PayErrorInfo.PAY_ORDER_NOT_FOUND);
        }
        // 2.判断是否已经支付成功
        if (!payOrder.success()) {
            // 订单尚未支付，无法退款
            throw new BizIllegalException(PayErrorInfo.PAY_ORDER_NOT_SUCCESS);
        }

        // 3.查询当前订单是否已经有退款单
        RefundOrder oldRefundOrder = queryByBizRefundOrder(refundApplyDTO.getBizRefundOrderNo());
        // 3.1.判断是否为空
        if (oldRefundOrder == null) {
            // 本订单第一次退款，需要生成新退款单
            RefundOrder refundOrder = BeanUtils.toBean(refundApplyDTO, RefundOrder.class);
            refundOrder.setRefundOrderNo(IdWorker.getId());
            refundOrder.setIsSplit(payOrder.getAmount().equals(refundApplyDTO.getRefundAmount()));
            refundOrder.setPayOrderNo(payOrder.getPayOrderNo());
            refundOrder.setTotalAmount(payOrder.getAmount());
            refundOrder.setPayChannelCode(payOrder.getPayChannelCode());
            save(refundOrder);
            return refundOrder;
        }

        // 3.2.判断退款是否已经成功，如果成功不能退款
        if (oldRefundOrder.success()) {
            throw new BizIllegalException(PayErrorInfo.REPEAT_REFUND_ORDER);
        }

        // 3.3.判断退款是否已经失败，如果失败直接结束
        if (oldRefundOrder.failed()) {
            throw new BizIllegalException(PayErrorInfo.REFUND_FAILED);
        }

        // 3.4.退款请求未提交，重新提交
        if (oldRefundOrder.notCommit()) {
            // 需要先更新退款数据
            oldRefundOrder.setRefundAmount(refundApplyDTO.getRefundAmount());
            updateById(oldRefundOrder);
            return oldRefundOrder;
        }

        // 3.5.退款正在进行中，什么都不做
        return null;
    }

    private RefundOrder queryByBizRefundOrder(Long bizRefundOrderId) {
        return lambdaQuery()
                .eq(RefundOrder::getBizRefundOrderNo, bizRefundOrderId)
                .one();
    }

    @Override
    public RefundResultDTO queryRefundResult(Long bizRefundOrderId) {
        // 1.查询退款单
        RefundOrder refundOrder = queryByBizRefundOrder(bizRefundOrderId);
        // 2.判断是否为空
        if (refundOrder == null) {
            return null;
        }
        // 3.判断退款单是否退款成功
        if (refundOrder.success()) {
            return RefundResultDTO.success()
                    .refundOrderNo(refundOrder.getRefundOrderNo())
                    .bizPayOrderId(refundOrder.getBizOrderNo())
                    .bizRefundOrderId(refundOrder.getBizRefundOrderNo())
                    .refundChannel(refundOrder.getRefundChannel())
                    .build();
        }
        // 4.判断退款单是否退款失败
        if (refundOrder.failed()) {
            return RefundResultDTO.failed()
                    .msg(refundOrder.getResultMsg())
                    .refundOrderNo(refundOrder.getRefundOrderNo())
                    .build();
        }
        // 5.退款状态未知，需求去第三方查询，选择查询渠道
        IPayService payService = payServiceChannels.get(refundOrder.getPayChannelCode());
        if (payService == null) {
            log.error("退款异常，支付渠道不存在，业务单号：{}", bizRefundOrderId);
            throw new BadRequestException(INVALID_PAY_CHANNEL);
        }
        // 6.去第三方查询一下
        RefundResponse refundResponse = payService.queryRefundStatus(
                refundOrder.getPayOrderNo().toString(), refundOrder.getRefundOrderNo().toString());
        // 6.1.更新数据库退款单状态
        updateRefundStatus(refundResponse, refundOrder.getId());

        // 6.2.返回结果
        RefundResultDTO refundResultDTO = transferRefundResult(refundResponse);
        refundResultDTO.setRefundOrderNo(refundOrder.getRefundOrderNo());
        refundResultDTO.setBizRefundOrderId(bizRefundOrderId);
        return refundResultDTO;
    }

    @Override
    public RefundOrder queryByRefundOrderNo(Long refundOrderNo) {
        return lambdaQuery()
                .eq(RefundOrder::getRefundOrderNo, refundOrderNo)
                .one();
    }

    @Override
    public PageDTO<RefundOrder> queryRefundingOrderByPage(int pageNo, int size) {
        // 1.分页和排序条件
        Page<RefundOrder> page = new Page<>(pageNo, size);
        page.addOrder(new OrderItem("id", true));
        // 2.查询
        Page<RefundOrder> result = lambdaQuery()
                .eq(RefundOrder::getStatus, RefundStatus.UN_KNOWN.getValue())
                .page(page);
        return PageDTO.of(result);
    }

    @Override
    public void checkRefundOrder(RefundOrder refundOrder) {
        // 1.获取退款渠道
        String payChannelCode = refundOrder.getPayChannelCode();
        IPayService payService = payServiceChannels.get(payChannelCode);
        if (payService == null) {
            log.error("支付渠道不存在，退款单号：{}", refundOrder.getId());
            // 异常订单，需要关闭支付单
            closeOrder(refundOrder.getId());
            return;
        }
        // 2.查询退款状态
        RefundResponse refundResponse = payService.queryRefundStatus(
                refundOrder.getPayOrderNo().toString(), refundOrder.getRefundOrderNo().toString());

        if (refundResponse.getStatus().equals(refundOrder.getStatus())) {
            // 退款状态没有变化，不做处理
            return;
        }

        // 3.更新数据库退款单状态
        updateRefundStatus(refundResponse, refundOrder.getId());

        // 4.发送MQ通知业务端
        rabbitMqHelper.send(
                MqConstants.Exchange.PAY_EXCHANGE,
                MqConstants.Key.REFUND_CHANGE,
                RefundResultDTO.success()
                        .refundOrderNo(refundOrder.getRefundOrderNo())
                        .bizPayOrderId(refundOrder.getBizOrderNo())
                        .bizRefundOrderId(refundOrder.getBizRefundOrderNo())
                        .refundChannel(refundOrder.getRefundChannel())
                        .build()
        );
    }

    @Override
    public RefundResultDTO queryRefundDetail(Long bizRefundOrderId) {
        // 1.查询退款单
        RefundOrder refundOrder = queryByBizRefundOrder(bizRefundOrderId);
        // 2.判断是否为空
        if (refundOrder == null) {
            throw new BadRequestException(PayErrorInfo.REFUND_ORDER_NOT_FOUND);
        }
        // 3.返回结果
        return RefundResultDTO.builder()
                .status(refundOrder.getStatus())
                .msg(refundOrder.getResultMsg())
                .bizRefundOrderId(bizRefundOrderId)
                .bizPayOrderId(refundOrder.getBizOrderNo())
                .payOrderNo(refundOrder.getPayOrderNo())
                .refundOrderNo(refundOrder.getRefundOrderNo())
                .payChannel(refundOrder.getRefundChannel())
                .build();
    }

    private void closeOrder(Long id) {
        RefundOrder refundOrder = new RefundOrder();
        refundOrder.setId(id);
        refundOrder.setStatus(RefundStatus.FAILED.getValue());
        updateById(refundOrder);
    }

    private RefundResultDTO transferRefundResult(RefundResponse refundResponse) {
        if (!refundResponse.getSuccess()) {
            return RefundResultDTO.failed()
                    .msg(refundResponse.getMsg()).refundChannel(refundResponse.getChannel()).build();
        }
        if (refundResponse.refundSuccess()) {
            return RefundResultDTO.success().refundChannel(refundResponse.getChannel()).build();
        }
        return RefundResultDTO.running()
                .msg(refundResponse.getMsg()).refundChannel(refundResponse.getChannel()).build();
    }
}
