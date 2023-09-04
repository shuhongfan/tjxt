package com.tianji.pay.service.impl;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tianji.common.autoconfigure.mq.RabbitMqHelper;
import com.tianji.common.autoconfigure.redisson.annotations.Lock;
import com.tianji.common.autoconfigure.redisson.enums.LockStrategy;
import com.tianji.common.constants.MqConstants;
import com.tianji.common.domain.dto.PageDTO;
import com.tianji.common.exceptions.BadRequestException;
import com.tianji.common.exceptions.BizIllegalException;
import com.tianji.common.utils.BeanUtils;
import com.tianji.common.utils.StringUtils;
import com.tianji.pay.constants.NotifyStatus;
import com.tianji.pay.domain.po.PayOrder;
import com.tianji.pay.mapper.PayOrderMapper;
import com.tianji.pay.sdk.constants.PayConstants;
import com.tianji.pay.sdk.constants.PayErrorInfo;
import com.tianji.pay.sdk.dto.PayApplyDTO;
import com.tianji.pay.sdk.dto.PayResultDTO;
import com.tianji.pay.service.IPayOrderService;
import com.tianji.pay.third.IPayService;
import com.tianji.pay.third.model.PayStatus;
import com.tianji.pay.third.model.PayStatusResponse;
import com.tianji.pay.third.model.PrepayResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Map;

import static com.tianji.pay.sdk.constants.PayErrorInfo.*;

/**
 * <p>
 * 支付订单 服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2022-08-26
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PayOrderServiceImpl extends ServiceImpl<PayOrderMapper, PayOrder> implements IPayOrderService {

    @Resource
    private Map<String, IPayService> payServiceChannels;
    private final RabbitMqHelper rabbitMqHelper;

    @Override
    @Lock(name = PayConstants.RedisKeyFormatter.PAY_APPLY, leaseTime = 3, autoUnlock = false)
    public String applyPayOrder(PayApplyDTO payApplyDTO) {
        log.debug("准备创建支付单，业务订单号：{}", payApplyDTO.getBizOrderNo());
        // 1.选择支付渠道
        IPayService payService = payServiceChannels.get(payApplyDTO.getPayChannelCode());
        if (payService == null) {
            log.error("用户选择的支付渠道不存在，业务单号：{}", payApplyDTO.getBizOrderNo());
            throw new BadRequestException(INVALID_PAY_CHANNEL);
        }

        // 2.幂等性校验
        PayOrder payOrder = checkIdempotent(payApplyDTO);
        if (StringUtils.isNotBlank(payOrder.getQrCodeUrl())) {
            log.debug("支付链接已经存在，不再重新创建，直接返回");
            return payOrder.getQrCodeUrl();
        }

        // 3.需要生成新支付链接，调用第三方，完成支付单下单
        PrepayResponse prepayResponse = payService.createPrepayOrder(
                payApplyDTO.getOrderInfo(), payOrder.getPayOrderNo().toString(), payOrder.getAmount());

        // 4.更新支付链接等信息到数据库
        updatePayResult2DB(prepayResponse, payOrder.getId());

        if (!prepayResponse.isSuccess()) {
            log.error("预创建支付单失败，详情：{}", prepayResponse.getDetail());
            throw new BizIllegalException(PayErrorInfo.CREATE_PAY_ORDER_FAILED);
        }
        // 5.返回支付链接
        log.debug("支付单生成成功，返回支付链接：{}", prepayResponse.getPayUrl());
        return prepayResponse.getPayUrl();
    }

    private void updatePayResult2DB(PrepayResponse prepayResponse, Long payOrderId) {
        try {
            lambdaUpdate()
                    .set(prepayResponse.isSuccess(), PayOrder::getQrCodeUrl, prepayResponse.getPayUrl())
                    .set(prepayResponse.isSuccess(), PayOrder::getStatus, PayStatus.WAIT_BUYER_PAY.getValue())
                    .set(!prepayResponse.isSuccess(), PayOrder::getResultCode, prepayResponse.getCode())
                    .set(!prepayResponse.isSuccess(), PayOrder::getResultMsg, prepayResponse.getMsg())
                    .eq(PayOrder::getId, payOrderId)
                    .update();
        } catch (Exception e) {
            log.error("更新支付单结果到数据时发生异常", e);
        }
    }

    private PayOrder buildPayOrder(PayApplyDTO payApplyDTO) {
        // 1.数据转换
        PayOrder payOrder = BeanUtils.toBean(payApplyDTO, PayOrder.class);
        // 2.初始化数据
        payOrder.setNotifyTimes(0);
        payOrder.setNotifyStatus(NotifyStatus.UN_CALL.getValue());
        payOrder.setPayOverTime(LocalDateTime.now().plusMinutes(120L));
        payOrder.setStatus(PayStatus.NOT_COMMIT.getValue());
        return payOrder;
    }

    private PayOrder checkIdempotent(PayApplyDTO payApplyDTO) {
        // 1.首先查询支付订单
        PayOrder oldOrder = queryByBizOrderNo(payApplyDTO.getBizOrderNo());
        // 2.判断是否存在
        if (oldOrder == null) {
            // 不存在支付单，说明是第一次，写入新的支付单并返回
            PayOrder payOrder = buildPayOrder(payApplyDTO);
            payOrder.setPayOrderNo(IdWorker.getId());
            save(payOrder);
            return payOrder;
        }
        // 3.旧单已经存在，判断是否支付成功
        if (PayStatus.TRADE_SUCCESS.equalsValue(oldOrder.getStatus())) {
            // 已经支付成功，抛出异常
            throw new BizIllegalException(PAY_ORDER_ALREADY_PAY_CODE, PAY_ORDER_ALREADY_PAY);
        }
        // 4.旧单已经存在，判断是否已经关闭
        if (PayStatus.TRADE_CLOSED.equalsValue(oldOrder.getStatus())) {
            // 已经关闭，抛出异常
            throw new BizIllegalException(PAY_ORDER_ALREADY_CLOSE_CODE, PAY_ORDER_ALREADY_CLOSE);
        }
        // 5.旧单已经存在，判断支付渠道是否一致
        if (!StringUtils.equals(oldOrder.getPayChannelCode(), payApplyDTO.getPayChannelCode())) {
            // 支付渠道不一致，需要重置数据，然后重新申请支付单
            PayOrder payOrder = buildPayOrder(payApplyDTO);
            payOrder.setId(oldOrder.getId());
            payOrder.setQrCodeUrl("");
            updateById(payOrder);
            payOrder.setPayOrderNo(oldOrder.getPayOrderNo());
            return payOrder;
        }
        // 6.旧单已经存在，且可能是未支付或未提交，且支付渠道一致，直接返回旧数据
        return oldOrder;
    }

    @Override
    public PayOrder queryByBizOrderNo(Long bizOrderNo) {
        return lambdaQuery()
                .eq(PayOrder::getBizOrderNo, bizOrderNo)
                .one();
    }

    @Override
    public PayResultDTO queryPayResult(Long bizOrderNo) {
        // 1.查询支付单
        PayOrder payOrder = queryByBizOrderNo(bizOrderNo);
        if (payOrder == null) {
            throw new BizIllegalException(PAY_ORDER_NOT_FOUND);
        }
        // 2.判断支付状态
        if (payOrder.success()) {
            // 2.1.支付成功
            return PayResultDTO.builder()
                    .payOrderNo(payOrder.getPayOrderNo())
                    .successTime(payOrder.getPaySuccessTime())
                    .payChannel(payOrder.getPayChannelCode())
                    .build();
        }
        // 2.2.未支付
        if (payOrder.notCommit() || payOrder.waitBuyerPay()) {
            return PayResultDTO.builder()
                    .status(PayStatus.WAIT_BUYER_PAY.getValue())
                    .build();
        }
        // 2.2.支付失败
        return PayResultDTO.builder()
                .status(PayStatus.TRADE_CLOSED.getValue())
                .msg(payOrder.getResultMsg())
                .build();
    }

    @Override
    public PayOrder queryByPayOrderNo(Long payOrderNo) {
        return lambdaQuery().eq(PayOrder::getPayOrderNo, payOrderNo).one();
    }

    @Override
    public boolean markPayOrderSuccess(Long id, LocalDateTime successTime) {
        return lambdaUpdate()
                .set(PayOrder::getStatus, PayStatus.TRADE_SUCCESS.getValue())
                .set(PayOrder::getNotifyStatus, NotifyStatus.CALLING.getValue())
                .set(PayOrder::getPaySuccessTime, successTime)
                .eq(PayOrder::getId, id)
                // 支付状态的乐观锁判断
                .in(PayOrder::getStatus, PayStatus.NOT_COMMIT.getValue(), PayStatus.WAIT_BUYER_PAY.getValue())
                .update();
    }

    @Override
    public PageDTO<PayOrder> queryPayingOrderByPage(int pageNo, int size) {
        // 1.分页和排序条件
        Page<PayOrder> page = new Page<>(pageNo, size);
        page.addOrder(new OrderItem("id", true));
        // 2.查询
        Page<PayOrder> result = lambdaQuery()
                .eq(PayOrder::getStatus, PayStatus.WAIT_BUYER_PAY.getValue())
                .page(page);
        return PageDTO.of(result);
    }

    @Override
    @Lock(name = PayConstants.RedisKeyFormatter.PAY_ORDER_CHECK_TASK, lockStrategy = LockStrategy.SKIP_AFTER_RETRY_TIMEOUT)
    public void checkPayOrder(PayOrder payOrder) {
        // 1.选择支付渠道
        IPayService payService = payServiceChannels.get(payOrder.getPayChannelCode());
        if (payService == null) {
            log.error("支付渠道不存在，业务单号：{}", payOrder.getBizOrderNo());
            // 异常订单，需要关闭支付单
            closeOrder(payOrder.getId());
            return;
        }
        // 2.判断订单是否超时
        if (payOrder.getPayOverTime().isBefore(LocalDateTime.now())) {
            log.debug("支付单{}已经超时，关闭订单", payOrder.getPayOrderNo());
            closeOrder(payOrder.getId());
            return;
        }
        // 3.查询支付状态
        PayStatusResponse response = payService.queryPayOrderStatus(payOrder.getPayOrderNo().toString());
        Integer payStatus = response.getPayStatus();
        // 3.1.判断是否查询失败或者正在支付
        if (!response.isSuccess() || PayStatus.WAIT_BUYER_PAY.equalsValue(payStatus)) {
            // 查询异常或正在支付，结束
            return;
        }
        // 3.2.判断支付状态是否变更
        if (payStatus.equals(payOrder.getStatus())) {
            // 支付状态没有变更
            return;
        }
        // 3.3.状态是支付成功或失败，直接更新订单状态
        updatePayStatus2DB(response, payOrder.getId());
        // 3.4.判断状态是否是成功，成功需要发送MQ消息通知
        if (PayStatus.TRADE_SUCCESS.equalsValue(response.getPayStatus())) {
            rabbitMqHelper.send(
                    MqConstants.Exchange.PAY_EXCHANGE,
                    MqConstants.Key.PAY_SUCCESS,
                    PayResultDTO.builder()
                            .payOrderNo(payOrder.getPayOrderNo())
                            .bizOrderId(payOrder.getBizOrderNo())
                            .payChannel(payOrder.getPayChannelCode())
                            .successTime(response.getSuccessTime())
                            .build()
            );
        }
    }

    private void updatePayStatus2DB(PayStatusResponse response, Long id) {
        try {
            lambdaUpdate()
                    .set(PayOrder::getStatus, response.getPayStatus())
                    .set(PayOrder::getResultCode, response.getCode() == null ? "" : response.getCode())
                    .set(PayOrder::getResultMsg, response.getMsg() == null ? "" : response.getMsg())
                    .eq(PayOrder::getId, id)
                    .update();
        } catch (Exception e) {
            log.error("更新支付单结果到数据时发生异常", e);
        }
    }

    private void closeOrder(Long id) {
        PayOrder payOrder = new PayOrder();
        payOrder.setId(id);
        payOrder.setStatus(PayStatus.TRADE_CLOSED.getValue());
        updateById(payOrder);
    }
}
