package com.tianji.pay.tasks;

import com.tianji.common.domain.dto.PageDTO;
import com.tianji.common.utils.StringUtils;
import com.tianji.pay.domain.po.RefundOrder;
import com.tianji.pay.service.IRefundOrderService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RefundOrderCheckTask {

    private final IRefundOrderService refundOrderService;

    @XxlJob("refundOrderCheckHandler")
    public void checkRefundOrderStatus() {
        // 1.获取分片信息
        int index = XxlJobHelper.getShardIndex() + 1;
        String jobParam = XxlJobHelper.getJobParam();
        int size = StringUtils.isNumeric(jobParam) ? Integer.parseInt(jobParam) : 10;
        // 2.查询需要处理的退款订单
        PageDTO<RefundOrder> result = refundOrderService.queryRefundingOrderByPage(index, size);
        if (result.isEmpty()) {
            return;
        }
        // 3.逐个检查退款单
        for (RefundOrder refundOrder : result.getList()) {
            try {
                refundOrderService.checkRefundOrder(refundOrder);
            } catch (Exception e) {
                log.error("处理退款订单状态异常：", e);
            }
        }
    }
}
