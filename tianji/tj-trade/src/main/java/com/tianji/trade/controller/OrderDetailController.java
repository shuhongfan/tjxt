package com.tianji.trade.controller;


import com.tianji.api.dto.course.CoursePurchaseInfoDTO;
import com.tianji.common.domain.dto.PageDTO;
import com.tianji.trade.domain.query.OrderDetailPageQuery;
import com.tianji.trade.domain.vo.OrderDetailAdminVO;
import com.tianji.trade.domain.vo.OrderDetailPageVO;
import com.tianji.trade.service.IOrderDetailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单明细 前端控制器
 * </p>
 *
 * @author 虎哥
 * @since 2022-08-29
 */

@Api(tags = "订单明细相关接口")
@RestController
@RequestMapping("/order-details")
@RequiredArgsConstructor
public class OrderDetailController {

    private final IOrderDetailService detailService;

    @ApiOperation("分页查询订单明细")
    @GetMapping("/page")
    public PageDTO<OrderDetailPageVO> queryDetailForPage(OrderDetailPageQuery pageQuery) {
        return detailService.queryDetailForPage(pageQuery);
    }

    @ApiOperation("根据订单明细id获取详细信息")
    @GetMapping("/{id}")
    public OrderDetailAdminVO queryOrdersDetailProgress( @ApiParam(value = "订单明细id")@PathVariable("id") Long id) {
        return detailService.queryOrdersDetailProgress(id);
    }

    @ApiOperation("校验课程是否购买，是否过期")
    @GetMapping("/course/{id}")
    public Boolean checkCourseOrderInfo(@PathVariable("id") Long courseId){
        return detailService.checkCourseOrderInfo(courseId);
    }

    @ApiOperation("统计课程报名人数")
    @GetMapping("/enrollNum")
    public Map<Long, Integer> countEnrollNumOfCourse(@RequestParam("courseIdList") List<Long> courseIdList){
        return detailService.countEnrollNumOfCourse(courseIdList);
    }

    @ApiOperation("统计学生报名课程数量")
    @GetMapping("/enrollCourse")
    public Map<Long, Integer> countEnrollCourseOfStudent(@RequestParam("studentIds") List<Long> studentIds){
        return detailService.countEnrollCourseOfStudent(studentIds);
    }

    @GetMapping("purchaseInfo")
    public CoursePurchaseInfoDTO getPurchaseInfoOfCourse(@RequestParam("courseId") Long courseId){
        return detailService.getPurchaseInfoOfCourse(courseId);
    }
}
