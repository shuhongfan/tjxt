package com.tianji.trade.controller;


import com.tianji.common.domain.dto.PageDTO;
import com.tianji.trade.constants.OrderCancelReason;
import com.tianji.trade.domain.dto.PlaceOrderDTO;
import com.tianji.trade.domain.query.OrderPageQuery;
import com.tianji.trade.domain.vo.OrderConfirmVO;
import com.tianji.trade.domain.vo.OrderPageVO;
import com.tianji.trade.domain.vo.OrderVO;
import com.tianji.trade.domain.vo.PlaceOrderResultVO;
import com.tianji.trade.service.IOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author 虎哥
 * @since 2022-08-29
 */
@Api(tags = "订单相关接口")
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final IOrderService orderService;

    @ApiOperation("分页查询我的订单")
    @GetMapping("page")
    public PageDTO<OrderPageVO> queryMyOrderPage(OrderPageQuery pageQuery){
        return orderService.queryMyOrderPage(pageQuery);
    }

    @ApiOperation("根据id查询订单详细信息")
    @GetMapping("/{id}")
    public OrderVO queryOrderById(@ApiParam ("订单id")@PathVariable("id") Long id){
        return orderService.queryOrderById(id);
    }

    @ApiOperation("查询订单支付状态")
    @GetMapping("/{id}/status")
    public PlaceOrderResultVO queryOrderStatus(@ApiParam("订单id") @PathVariable("id") Long orderId) {
        return orderService.queryOrderStatus(orderId);
    }

    @ApiOperation("预下单接口，生成订单id，确认订单可用优惠券信息")
    @GetMapping("prePlaceOrder")
    public OrderConfirmVO prePlaceOrder(@RequestParam("courseIds")List<Long> courseIds) {
        return orderService.prePlaceOrder(courseIds);
    }

    @ApiOperation("下单接口")
    @PostMapping("placeOrder")
    public PlaceOrderResultVO placeOrder(@RequestBody @Validated PlaceOrderDTO placeOrderDTO) {
        return orderService.placeOrder(placeOrderDTO);
    }

    @ApiOperation("免费课立刻报名接口")
    @PostMapping("/freeCourse/{courseId}")
    public PlaceOrderResultVO enrolledFreeCourse(@ApiParam("免费课程id") @PathVariable("courseId") Long courseId) {
        return orderService.enrolledFreeCourse(courseId);
    }

    @ApiOperation("取消订单接口")
    @PutMapping("/{id}/cancel")
    public void cancelOrder(@ApiParam("要取消订单的id") @PathVariable("id") Long orderId){
        orderService.cancelOrder(orderId, OrderCancelReason.USER_CANCEL);
    }

    @ApiOperation("删除订单接口")
    @DeleteMapping("/{id}")
    public void deleteOrder(@ApiParam("要删除的订单id") @PathVariable("id") Long id) {
        orderService.deleteOrder(id);
    }
}
