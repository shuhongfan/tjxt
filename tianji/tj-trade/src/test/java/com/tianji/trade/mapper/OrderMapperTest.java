package com.tianji.trade.mapper;

import com.tianji.trade.domain.po.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OrderMapperTest {

    @Autowired
    private OrderMapper orderMapper;

    @Test
    void getById() {
        Order order = orderMapper.getById(1L);
        System.out.println("order = " + order);
    }
}