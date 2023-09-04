package com.tianji.pay.sdk.constants;

public interface PayConstants {
    String ALI_CHANNEL_CODE = "aliPay";
    String WX_CHANNEL_CODE = "wxPay";

    /**
     * redis key前缀
     */
   interface RedisKeyFormatter {
        String PAY_APPLY = "pay:apply:bizOrderNo:#{payApplyDTO.bizOrderNo}";
        String PAY_NOTIFY = "pay:notify:payOrderNo:#{tradingOrderNo}";
        String PAY_ORDER_CHECK_TASK = "pay:notify:payOrderNo:#{payOrder.payOrderNo}";

        String REFUND_APPLY = "pay:refund:bizOrderNo:#{refundApplyDTO.bizOrderNo}";

        String PAY_GET_NOTIFY_LOCK = "PAY:GET_NOTIFY_ORDERS";
        String REFUND_NOTIFY = "REFUND:NOTIFY_#{refundOrder.id}";
        String REFUND_GET_NOFIGY_LOCK = "REFUND:GET_NOTIFY_ORDERS";
        String REFUND_QUERY_TASK = "PAY:QUERY_#{refundOrderId}";
    }
}
