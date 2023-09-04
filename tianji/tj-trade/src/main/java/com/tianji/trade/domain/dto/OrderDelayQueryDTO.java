package com.tianji.trade.domain.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单状态延迟查询的MQ通知消息
 */
@Data
public class OrderDelayQueryDTO {
    /**
     * 订单id
     */
    private Long orderId;
    /**
     * 延迟通知的间隔，默认是3秒、5秒、15秒、30秒、60秒、2分钟共6次延迟查询，中间成功后可以随时取消任务
     */
    private List<Long> delayMillis;

    public static OrderDelayQueryDTO init(Long orderId){
        OrderDelayQueryDTO dto = new OrderDelayQueryDTO();
        dto.setOrderId(orderId);
        List<Long> list = new ArrayList<>(6);
        list.add(3000L);
        list.add(5000L);
        list.add(15000L);
        list.add(30000L);
        list.add(60000L);
        list.add(120000L);
        list.add(1567000L); // 30分钟检查，如果依然为支付则取消订单
        dto.setDelayMillis(list);
        return dto;
    }
    public long removeFirst(){
        return delayMillis.remove(0);
    }
}
