package com.tianji.trade.constants;

public interface TradeErrorInfo {

    String CARTS_FULL = "用户购物车课程不能超过{}";
    String COURSE_NOT_EXISTS = "课程不存在";
    String COURSE_EXPIRED = "课程已过期";
    String COURSE_NOT_FOR_SALE = "课程无法购买";
    String COURSE_NOT_FREE = "课程不是免费课程";

    String PLACE_ORDER_FAILED = "下单失败";
    String ORDER_NOT_EXISTS = "订单不存在";
    String ORDER_ALREADY_FINISH = "订单已经支付或退款";
    String ORDER_OVER_TIME = "订单已经超时";
    String ORDER_CANNOT_REFUND = "订单未支付或已关闭";

    String NO_AUTH_REFUND = "无权申请退款";
    String REFUND_TOO_MANY_TIMES = "退款次数太多";
    String REFUND_IN_PROGRESS = "有其它未完成的退款进程";
    String REFUND_NOT_EXISTS = "退款记录不存在";
    String REFUND_APPROVED = "退款申请已经处理过了";
    String FREE_COURSE_CANNOT_REFUND = "免费课程不能退款";

}
