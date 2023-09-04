package com.tianji.promotion.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tianji.common.domain.dto.PageDTO;
import com.tianji.common.utils.CollUtils;
import com.tianji.promotion.domain.po.Coupon;
import com.tianji.promotion.domain.po.ExchangeCode;
import com.tianji.promotion.domain.query.CodeQuery;
import com.tianji.promotion.domain.vo.ExchangeCodeVO;
import com.tianji.promotion.mapper.ExchangeCodeMapper;
import com.tianji.promotion.service.IExchangeCodeService;
import com.tianji.promotion.utils.CodeUtil;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.tianji.promotion.constants.PromotionConstants.*;

/**
 * <p>
 * 兑换码 服务实现类
 * </p>
 *
 * @author 虎哥
 */
@Service
public class ExchangeCodeServiceImpl extends ServiceImpl<ExchangeCodeMapper, ExchangeCode> implements IExchangeCodeService {

    private final StringRedisTemplate redisTemplate;
    private final BoundValueOperations<String, String> serialOps;

    public ExchangeCodeServiceImpl(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.serialOps = redisTemplate.boundValueOps(COUPON_CODE_SERIAL_KEY);
    }

    @Override
    @Async("generateExchangeCodeExecutor")
    public void asyncGenerateCode(Coupon coupon) {
        // 发放数量
        Integer totalNum = coupon.getTotalNum();
        // 1.获取Redis自增序列号
        Long result = serialOps.increment(totalNum);
        if (result == null) {
            return;
        }
        int maxSerialNum = result.intValue();
        List<ExchangeCode> list = new ArrayList<>(totalNum);
        for (int serialNum = maxSerialNum - totalNum + 1; serialNum <= maxSerialNum; serialNum++) {
            // 2.生成兑换码
            String code = CodeUtil.generateCode(serialNum, coupon.getId());
            ExchangeCode e = new ExchangeCode();
            e.setCode(code);
            e.setId(serialNum);
            e.setExchangeTargetId(coupon.getId());
            e.setExpiredTime(coupon.getIssueEndTime());
            list.add(e);
        }
        // 3.保存数据库
        saveBatch(list);

        // 4.写入Redis缓存，member：couponId，score：兑换码的最大序列号
        redisTemplate.opsForZSet().add(COUPON_RANGE_KEY, coupon.getId().toString(), maxSerialNum);
    }

    @Override
    public boolean updateExchangeMark(long serialNum, boolean mark) {
        Boolean boo = redisTemplate.opsForValue().setBit(COUPON_CODE_MAP_KEY, serialNum, mark);
        return boo != null && boo;
    }

    @Override
    public PageDTO<ExchangeCodeVO> queryCodePage(CodeQuery query) {
        // 1.分页查询兑换码
        Page<ExchangeCode> page = lambdaQuery()
                .eq(ExchangeCode::getStatus, query.getStatus())
                .eq(ExchangeCode::getExchangeTargetId, query.getCouponId())
                .page(query.toMpPage());
        // 2.返回数据
        return PageDTO.of(page, c -> new ExchangeCodeVO(c.getId(), c.getCode()));
    }

    @Override
    public Long exchangeTargetId(long serialNum) {
        // 1.查询score值比当前序列号大的第一个优惠券
        Set<String> results = redisTemplate.opsForZSet().rangeByScore(
                COUPON_RANGE_KEY, serialNum, serialNum + 5000, 0L, 1L);
        if (CollUtils.isEmpty(results)) {
            return null;
        }
        // 2.数据转换
        String next = results.iterator().next();
        return Long.parseLong(next);
    }
}
