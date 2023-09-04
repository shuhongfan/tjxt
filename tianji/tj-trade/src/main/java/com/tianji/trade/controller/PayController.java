package com.tianji.trade.controller;

import com.tianji.trade.domain.dto.PayApplyFormDTO;
import com.tianji.trade.domain.vo.PayChannelVO;
import com.tianji.trade.service.IPayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "支付相关接口")
@RestController
@RequestMapping("pay")
@RequiredArgsConstructor
public class PayController {

    private final IPayService payService;

    @PostMapping("/order")
    @ApiOperation(value = "支付申请,返回支付二维码url")
    public String applyPayOrder(@RequestBody PayApplyFormDTO payApply) {
        return payService.applyPayOrder(payApply);
    }

    @GetMapping("/channels")
    @ApiOperation("获取支付渠道列表接口")
    public List<PayChannelVO> queryPayChannels() {
        return payService.queryPayChannels();
    }

}