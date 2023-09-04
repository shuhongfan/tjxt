package com.tianji.search.mq;

import com.tianji.api.dto.trade.OrderBasicDTO;
import com.tianji.common.utils.CollUtils;
import com.tianji.search.service.ICourseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.tianji.common.constants.MqConstants.Exchange.ORDER_EXCHANGE;
import static com.tianji.common.constants.MqConstants.Key.ORDER_PAY_KEY;
import static com.tianji.common.constants.MqConstants.Key.ORDER_REFUND_KEY;

@Slf4j
@Component
public class OrderEventListener {

    @Autowired
    private ICourseService courseService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "search.order.pay.queue", durable = "true"),
            exchange = @Exchange(name = ORDER_EXCHANGE, type = ExchangeTypes.TOPIC),
            key = ORDER_PAY_KEY
    ))
    public void listenOrderPay(OrderBasicDTO order) {
        if (order == null || order.getUserId() == null || CollUtils.isEmpty(order.getCourseIds())) {
            log.debug("订单支付，异常消息，信息未空");
            return;
        }
        log.debug("处理订单支付消息：{}", order);
        courseService.updateCourseSold(order.getCourseIds(), 1);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "search.order.refund.queue", durable = "true"),
            exchange = @Exchange(name = ORDER_EXCHANGE, type = ExchangeTypes.TOPIC),
            key = ORDER_REFUND_KEY
    ))
    public void listenOrderRefund(OrderBasicDTO order) {
        if (order == null || order.getUserId() == null || CollUtils.isEmpty(order.getCourseIds())) {
            log.debug("订单退款，异常消息，信息未空");
            return;
        }
        log.debug("处理订单退款消息：{}", order);
        courseService.updateCourseSold(order.getCourseIds(), -1);
    }
}
