package com.tianji.pay.sdk.constants;

public interface PayErrorInfo {
    String PAY_ORDER_ALREADY_PAY = "支付单已经支付";
    String PAY_ORDER_ALREADY_CLOSE = "支付单已经关闭";
    String PAY_ORDER_NOT_FOUND = "支付单不存在";

    String PAY_ORDER_NOT_SUCCESS = "未支付订单无法退款";

    String INVALID_PAY_CHANNEL = "支付渠道码无效";
    String INVALID_PAY_TYPE = "不支持的支付类型";

    String CREATE_PAY_ORDER_FAILED = "创建支付单失败";

    String INVALID_NOTIFY_PARAM = "无效的通知参数";

    String REPEAT_REFUND_ORDER = "不能重复退款";
    String REFUND_FAILED = "退款已经失败";

    String REFUND_ORDER_NOT_FOUND = "退款单不存在";


    int PAY_ORDER_ALREADY_PAY_CODE = 1000;
    int PAY_ORDER_ALREADY_CLOSE_CODE = 2000;
}
