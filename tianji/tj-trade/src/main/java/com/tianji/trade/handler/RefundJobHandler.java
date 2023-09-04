package com.tianji.trade.handler;

import com.tianji.trade.domain.po.RefundApply;
import com.tianji.trade.service.IRefundApplyService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RefundJobHandler {

    private final IRefundApplyService refundApplyService;

    @XxlJob("refundRequestJobHandler")
    public void handleRefundRequest(){
        // 1.获取分片信息，作为页码，每页最多查询 2条，避免退款申请过于频繁
        int index = XxlJobHelper.getShardIndex() + 1;
        int size = 2;
        // 2.分页查询审批通过的退款申请
        List<RefundApply> list = refundApplyService.queryApplyToSend(index, size);
        // 3.循环处理退款申请
        for (RefundApply refundApply : list) {
            // 3.1.检查退款单状态，是否已经退款结束
            boolean refundFinished = refundApplyService.checkRefundStatus(refundApply);
            if(refundFinished){
                continue;
            }
            // 3.2.发送退款申请
            refundApplyService.sendRefundRequest(refundApply);
        }
    }
}
