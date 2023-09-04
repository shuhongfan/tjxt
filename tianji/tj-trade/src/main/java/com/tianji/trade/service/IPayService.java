package com.tianji.trade.service;

import com.tianji.trade.domain.dto.OrderDelayQueryDTO;
import com.tianji.trade.domain.dto.PayApplyFormDTO;
import com.tianji.trade.domain.vo.PayChannelVO;

import java.util.List;

public interface IPayService {
    List<PayChannelVO> queryPayChannels();

    String applyPayOrder(PayApplyFormDTO payApply);

    void queryPayResult(OrderDelayQueryDTO message);
}
