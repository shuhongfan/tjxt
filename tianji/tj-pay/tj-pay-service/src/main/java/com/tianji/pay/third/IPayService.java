package com.tianji.pay.third;

import com.tianji.pay.third.model.PayStatusResponse;
import com.tianji.pay.third.model.PrepayResponse;
import com.tianji.pay.third.model.RefundResponse;

/**
 * 统一支付服务接口
 */
public interface IPayService {

    PrepayResponse createPrepayOrder(String title, String orderNo, Integer amount);

    PayStatusResponse queryPayOrderStatus(String payOrderNo);

    RefundResponse refundOrder(String payOrderNo, String refundOrderNo, Integer refundAmount, Integer totalAmount);

    RefundResponse queryRefundStatus(String orderNo, String refundOrderNo);
}
