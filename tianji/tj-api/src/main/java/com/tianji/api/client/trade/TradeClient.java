package com.tianji.api.client.trade;

import com.tianji.api.client.trade.fallback.TradeClientFallback;
import com.tianji.api.dto.course.CoursePurchaseInfoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(value = "trade-service", fallbackFactory = TradeClientFallback.class)
public interface TradeClient {
    /**
     * 统计指定课程的报名人数
     * @param courseIdList 课程id集合
     * @return 统计结果
     */
    @GetMapping("/order-details/enrollNum")
    Map<Long, Integer> countEnrollNumOfCourse(@RequestParam("courseIdList") List<Long> courseIdList);

    /**
     * 统计指定学生的报名课程数量
     * @param studentIds 学生id集合
     * @return 统计结果
     */
    @GetMapping("/order-details/enrollCourse")
    Map<Long, Integer> countEnrollCourseOfStudent(@RequestParam("studentIds") List<Long> studentIds);

    /**
     * 检查当前用户是否报名指定课程
     * @param id 课程id
     * @return 是否报名
     */
    @GetMapping("/order-details/course/{id}")
    Boolean checkMyLesson(@PathVariable("id") Long id);

    /**
     * 统计课程购买、退款状态
     * @param courseId 课程id
     * @return 统计结果
     */
    @GetMapping("/order-details/purchaseInfo")
    CoursePurchaseInfoDTO getPurchaseInfoOfCourse(@RequestParam("courseId") Long courseId);
}
