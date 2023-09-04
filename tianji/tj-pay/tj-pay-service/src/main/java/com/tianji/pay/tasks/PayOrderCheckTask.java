package com.tianji.pay.tasks;

import com.tianji.common.domain.dto.PageDTO;
import com.tianji.common.utils.StringUtils;
import com.tianji.pay.domain.po.PayOrder;
import com.tianji.pay.service.IPayOrderService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PayOrderCheckTask {

    private final IPayOrderService payOrderService;

    @XxlJob("payOrderCheckHandler")
    public void checkPayOrderStatus() {
        // 1.获取分片信息
        int index = XxlJobHelper.getShardIndex() + 1;
        String jobParam = XxlJobHelper.getJobParam();
        int size = StringUtils.isNumeric(jobParam) ? Integer.parseInt(jobParam) : 10;
        // 2.查询需要处理的支付订单
        PageDTO<PayOrder> result = payOrderService.queryPayingOrderByPage(index, size);
        if (result.isEmpty()) {
            return;
        }
        // 3.逐个检查支付单
        for (PayOrder payOrder : result.getList()) {
            try {
                payOrderService.checkPayOrder(payOrder);
            } catch (Exception e) {
                log.error("处理订单支付状态异常：", e);
            }
        }
    }
}
