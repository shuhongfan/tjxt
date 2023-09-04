package com.tianji.pay.third.ali;

import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.kernel.util.ResponseChecker;
import com.alipay.easysdk.payment.common.models.AlipayTradeFastpayRefundQueryResponse;
import com.alipay.easysdk.payment.common.models.AlipayTradeQueryResponse;
import com.alipay.easysdk.payment.common.models.AlipayTradeRefundResponse;
import com.alipay.easysdk.payment.common.models.TradeFundBill;
import com.alipay.easysdk.payment.facetoface.models.AlipayTradePrecreateResponse;
import com.tianji.common.exceptions.CommonException;
import com.tianji.common.utils.CollUtils;
import com.tianji.common.utils.DateUtils;
import com.tianji.common.utils.StringUtils;
import com.tianji.pay.sdk.constants.PayConstants;
import com.tianji.pay.third.CommonPayProperties;
import com.tianji.pay.third.IPayService;
import com.tianji.pay.third.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

import static com.tianji.pay.sdk.constants.PayConstants.ALI_CHANNEL_CODE;

@Slf4j
@Service(ALI_CHANNEL_CODE)
@RequiredArgsConstructor
public class AliPayService implements IPayService {

    private final CommonPayProperties commonPayProperties;

    @Override
    public PrepayResponse createPrepayOrder(String title, String orderNo, Integer amount) {
        // 1.动态获取回调地址
        String notifyUrl = commonPayProperties.getNotifyHost() + "/notify/" + PayConstants.ALI_CHANNEL_CODE;

        // 2. 发起API调用（以创建当面付收款二维码为例）
        AlipayTradePrecreateResponse response = null;
        try {
            response = Factory.Payment.FaceToFace()
                    .asyncNotify(notifyUrl)
                    .preCreate(title, orderNo, transferAmount2String(amount));
        } catch (Exception e) {
            log.error("支付宝预下单失败，订单id：{}", orderNo, e);
            throw new CommonException("支付宝预下单失败", e);
        }
        // 3.处理响应
        PrepayResponse.PrepayResponseBuilder builder = PrepayResponse.builder();
        if (ResponseChecker.success(response)) {
            // 3.1.响应结果正常
            builder.success(true).payUrl(response.getQrCode());
        } else {
            // 3.2.响应结果异常
            builder.success(false).code(response.getCode()).msg(response.getMsg());
        }
        return builder.build();
    }



    @Override
    public PayStatusResponse queryPayOrderStatus(String payOrderNo) {
        // 1.发起请求
        AlipayTradeQueryResponse response = null;
        try {
            response = Factory.Payment.Common().query(payOrderNo);
        } catch (Exception e) {
            log.error("支付宝查询支付单状态失败，订单id：{}", payOrderNo, e);
            throw new CommonException("支付宝查询支付单状态失败", e);
        }
        // 2.解析
        if (!ResponseChecker.success(response)) {
            // 2.1.响应结果异常
            return PayStatusResponse.builder().success(false).code(response.getCode()).msg(response.getMsg()).build();
        }
        // 2.2.响应结果正常
        String success_time = response.getSendPayDate();
        LocalDateTime successTime = StringUtils.isBlank(success_time) ?
                LocalDateTime.now() : DateUtils.parse(success_time, DateUtils.DEFAULT_DATE_TIME_FORMAT);
        return PayStatusResponse.builder().success(true)
                        .payStatus(PayStatus.valueOf(response.getTradeStatus()).getValue())
                        .payOrderNo(response.getOutTradeNo())
                        .totalAmount(transferStringAmount2Int(response.getTotalAmount()))
                        .successTime(successTime)
                        .build();
    }

    @Override
    public RefundResponse refundOrder(String payOrderNo, String refundOrderNo, Integer refundAmount, Integer totalAmount) {
        // 1.发起请求
        AlipayTradeRefundResponse response = null;
        try {
            response = Factory.Payment.Common()
                    .optional("query_options", List.of("refund_detail_item_list"))
                    .optional("out_request_no", refundOrderNo)
                    .refund(payOrderNo, transferAmount2String(refundAmount));
        } catch (Exception e) {
            log.error("支付宝申请退款失败，订单id：{} ，退款订单id：{}", payOrderNo, refundOrderNo, e);
            throw new CommonException("支付宝申请退款失败", e);
        }
        // 2.解析响应
        if (!ResponseChecker.success(response)) {
            // 2.1.响应结果异常
            return RefundResponse.builder().success(false).code(response.getSubCode()).msg(response.getSubMsg()).build();
        }
        // 2.2.响应结果正常，获取响应细节数据
        List<TradeFundBill> refundDetailItemList = response.getRefundDetailItemList();
        boolean hasDetail = CollUtils.isEmpty(refundDetailItemList);
        // 2.3.获取退款成功标示
        boolean success = StringUtils.equals(response.getFundChange(), "Y");
        return RefundResponse.builder()
                .success(true)
                .status(success ? RefundStatus.SUCCESS.getValue(): RefundStatus.UN_KNOWN.getValue())
                .channel(hasDetail ? null : refundDetailItemList.get(0).fundChannel)
                .amount(hasDetail ? null : transferStringAmount2Int(refundDetailItemList.get(0).getAmount()))
                .build();
    }

    @Override
    public RefundResponse queryRefundStatus(String orderNo, String refundOrderNo) {
        // 1.发起请求
        AlipayTradeFastpayRefundQueryResponse response = null;
        try {
            response = Factory.Payment.Common().queryRefund(orderNo, refundOrderNo);
        } catch (Exception e) {
            log.error("支付宝查询退款单状态失败，订单id：{}", orderNo, e);
            throw new CommonException("支付宝查询退款单状态失败", e);
        }
        // 2.解析
        if (ResponseChecker.success(response)) {
            // 2.1.响应结果异常
            return RefundResponse.builder().success(false).code(response.getCode()).msg(response.getMsg()).build();
        }
        // 2.2.响应结果正常
        String refundStatus = response.getRefundStatus();
        boolean refundSuccess = "REFUND_SUCCESS".equals(refundStatus);

        List<TradeFundBill> details = response.getRefundDetailItemList();
        return RefundResponse.builder()
                .success(true)
                .status(refundSuccess ? 2 : 1)
                .channel(refundSuccess ? details.get(0).fundChannel : null)
                .amount(refundSuccess ? transferStringAmount2Int(details.get(0).getAmount()) : 0)
                .build();
    }


    public static int transferStringAmount2Int(String totalAmount) {
        return new BigDecimal(totalAmount).multiply(BigDecimal.valueOf(100)).intValue();
    }
    public static String transferAmount2String(Integer amount) {
        BigDecimal b = new BigDecimal(amount);
        BigDecimal result = b.divide(new BigDecimal(100), new MathContext(2, RoundingMode.HALF_UP));
        return result.toString();
    }
}
