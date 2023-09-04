package com.tianji.learning.controller;

import com.tianji.learning.domain.vo.PointsStatisticsVO;
import com.tianji.learning.service.IPointsRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 学习积分记录，每个月底清零 控制器
 * </p>
 *
 * @author 虎哥
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/points")
@Api(tags = "积分相关接口")
public class PointsRecordController {

    private final IPointsRecordService pointsRecordService;

    @ApiOperation("查询我的今日积分")
    @GetMapping("today")
    public List<PointsStatisticsVO> queryMyPointsToday(){
        return pointsRecordService.queryMyPointsToday();
    }
}
