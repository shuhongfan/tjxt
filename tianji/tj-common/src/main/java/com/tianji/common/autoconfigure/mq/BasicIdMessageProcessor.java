package com.tianji.common.autoconfigure.mq;

import cn.hutool.core.lang.UUID;
import org.slf4j.MDC;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;

import static com.tianji.common.constants.Constant.REQUEST_ID_HEADER;

public class BasicIdMessageProcessor implements MessagePostProcessor {
    @Override
    public Message postProcessMessage(Message message) throws AmqpException {
        String requestId = MDC.get(REQUEST_ID_HEADER);
        if (requestId == null) {
            requestId = UUID.randomUUID().toString(true);
        }
        // 写入RequestID标示
        message.getMessageProperties().setHeader(REQUEST_ID_HEADER, requestId);
        return message;
    }
}
