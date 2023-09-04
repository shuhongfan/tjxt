package com.tianji.common.constants;

public interface MqConstants {
    interface Exchange{
        /*课程有关的交换机*/
        String COURSE_EXCHANGE = "course.topic";

        /*订单有关的交换机*/
        String ORDER_EXCHANGE = "order.topic";

        /*学习有关的交换机*/
        String LEARNING_EXCHANGE = "learning.topic";

        /*信息中心短信相关的交换机*/
        String SMS_EXCHANGE = "sms.direct";

        /*异常信息的交换机*/
        String ERROR_EXCHANGE = "error.topic";

        /*支付有关的交换机*/
        String PAY_EXCHANGE = "pay.topic";
        /*交易服务延迟任务交换机*/
        String TRADE_DELAY_EXCHANGE = "trade.delay.topic";

         /*点赞记录有关的交换机*/
        String LIKE_RECORD_EXCHANGE = "like.record.topic";

        /*优惠促销有关的交换机*/
        String PROMOTION_EXCHANGE = "promotion.topic";
    }
    interface Queue {
        String ERROR_QUEUE_TEMPLATE = "error.{}.queue";
    }
    interface Key{
        /*课程有关的 RoutingKey*/
        String COURSE_NEW_KEY = "course.new";
        String COURSE_UP_KEY = "course.up";
        String COURSE_DOWN_KEY = "course.down";
        String COURSE_EXPIRE_KEY = "course.expire";
        String COURSE_DELETE_KEY = "course.delete";

        /*订单有关的RoutingKey*/
        String ORDER_PAY_KEY = "order.pay";
        String ORDER_REFUND_KEY = "order.refund";

        /*积分相关RoutingKey*/
        /* 写回答 */
        String WRITE_REPLY = "reply.new";
        /* 签到 */
        String SIGN_IN = "sign.in";
        /* 学习视频 */
        String LEARN_SECTION = "section.learned";
        /* 写笔记 */
        String WRITE_NOTE = "note.new";
        /* 笔记被采集 */
        String NOTE_GATHERED = "note.gathered";

        /*点赞的RoutingKey*/
        String LIKED_TIMES_KEY_TEMPLATE = "{}.times.changed";
        /*问答*/
        String QA_LIKED_TIMES_KEY = "QA.times.changed";
        /*笔记*/
        String NOTE_LIKED_TIMES_KEY = "NOTE.times.changed";

        /*短信系统发送短信*/
        String SMS_MESSAGE = "sms.message";

        /*异常RoutingKey的前缀*/
        String ERROR_KEY_PREFIX = "error.";
        String DEFAULT_ERROR_KEY = "error.#";

        /*支付有关的key*/
        String PAY_SUCCESS = "pay.success";
        String REFUND_CHANGE = "refund.status.change";

        String ORDER_DELAY_KEY = "delay.order.query";

        /*领取优惠券的key*/
        String COUPON_RECEIVE = "coupon.receive";
    }
}
