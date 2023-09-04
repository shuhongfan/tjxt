package com.tianji.promotion.handler;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tianji.common.utils.CollUtils;
import com.tianji.promotion.domain.po.Coupon;
import com.tianji.promotion.enums.CouponStatus;
import com.tianji.promotion.service.ICouponService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CouponIssueTaskHandler {

    private final ICouponService couponService;

    @XxlJob("couponIssueJobHandler")
    public void handleCouponIssueJob(){
        // 1.获取分片信息，作为页码，每页最多查询 20条
        int index = XxlJobHelper.getShardIndex() + 1;
        int size = Integer.parseInt(XxlJobHelper.getJobParam());
        // 2.查询<<未开始>>的优惠券
        Page<Coupon> page = couponService.lambdaQuery()
                .eq(Coupon::getStatus, CouponStatus.UN_ISSUE)
                .le(Coupon::getTermBeginTime, LocalDateTime.now())
                .page(new Page<>(index, size));
        // 3.发放优惠券
        List<Coupon> records = page.getRecords();
        if (CollUtils.isEmpty(records)) {
            return;
        }
        couponService.beginIssueBatch(records);
    }
}
