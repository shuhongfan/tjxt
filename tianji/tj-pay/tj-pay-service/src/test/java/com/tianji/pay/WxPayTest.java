package com.tianji.pay;

import com.tianji.pay.third.model.PayStatusResponse;
import com.tianji.pay.third.model.PrepayResponse;
import com.tianji.pay.third.model.RefundResponse;
import com.tianji.pay.third.wx.WxPayClient;
import com.tianji.pay.third.wx.WxPayService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class WxPayTest {
    @Autowired
    private WxPayClient wxPayClient;
    @Autowired
    private WxPayService payService;

    String orderNo = "21294121545";
    String refundOrderNo1 = "2129412671345";
    String refundOrderNo2 = "2129412921345";

    @Test
    void testPreCreate() {
        PrepayResponse response = payService.createPrepayOrder("Iphone 12 128G", orderNo, 200);
        System.out.println("response = " + response);
    }

    @Test
    void testQueryPayStatus() {
        PayStatusResponse payStatusResponse = payService.queryPayOrderStatus(orderNo);
        System.out.println("payStatusResponse = " + payStatusResponse);
    }

    @Test
    void testRefund() {
        RefundResponse refundResponse = payService.refundOrder(orderNo, refundOrderNo1, 100, 200);
        System.out.println("refundResponse = " + refundResponse);
    }

    @Test
    void testQueryRefund() {
        RefundResponse refundResponse = payService.queryRefundStatus(orderNo, refundOrderNo1);
        System.out.println("refundResponse = " + refundResponse);
    }
}
