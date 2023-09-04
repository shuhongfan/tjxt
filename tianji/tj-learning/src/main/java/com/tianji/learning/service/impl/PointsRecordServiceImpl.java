package com.tianji.learning.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tianji.common.utils.CollUtils;
import com.tianji.common.utils.DateUtils;
import com.tianji.common.utils.UserContext;
import com.tianji.learning.constants.RedisConstants;
import com.tianji.learning.domain.po.PointsRecord;
import com.tianji.learning.domain.vo.PointsStatisticsVO;
import com.tianji.learning.enums.PointsRecordType;
import com.tianji.learning.mapper.PointsRecordMapper;
import com.tianji.learning.service.IPointsRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 学习积分记录，每个月底清零 服务实现类
 * </p>
 *
 * @author 虎哥
 */
@Service
@RequiredArgsConstructor
public class PointsRecordServiceImpl extends ServiceImpl<PointsRecordMapper, PointsRecord> implements IPointsRecordService {

    private final StringRedisTemplate redisTemplate;

    @Override
    public void addPointsRecord(Long userId, int points, PointsRecordType type) {
        LocalDateTime now = LocalDateTime.now();
        int maxPoints = type.getMaxPoints();
        // 1.判断当前方式有没有积分上限
        int realPoints = points;
        if(maxPoints > 0) {
            // 2.有，则需要判断是否超过上限
            LocalDateTime begin = DateUtils.getDayStartTime(now);
            LocalDateTime end = DateUtils.getDayEndTime(now);
            // 2.1.查询今日已得积分
            int currentPoints = queryUserPointsByTypeAndDate(userId, type, begin, end);
            // 2.2.判断是否超过上限
            if(currentPoints >= maxPoints) {
                // 2.3.超过，直接结束
                return;
            }
            // 2.4.没超过，保存积分记录
            if(currentPoints + points > maxPoints){
                realPoints = maxPoints - currentPoints;
            }
        }
        // 3.没有，直接保存积分记录
        PointsRecord p = new PointsRecord();
        p.setPoints(realPoints);
        p.setUserId(userId);
        p.setType(type);
        save(p);
        // 4.更新总积分到Redis
        String key = RedisConstants.POINTS_BOARD_KEY_PREFIX + now.format(DateUtils.POINTS_BOARD_SUFFIX_FORMATTER);
        redisTemplate.opsForZSet().incrementScore(key, userId.toString(), realPoints);
    }

    @Override
    public List<PointsStatisticsVO> queryMyPointsToday() {
        // 1.获取用户
        Long userId = UserContext.getUser();
        // 2.获取日期
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime begin = DateUtils.getDayStartTime(now);
        LocalDateTime end = DateUtils.getDayEndTime(now);
        // 3.构建查询条件
        QueryWrapper<PointsRecord> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .eq(PointsRecord::getUserId, userId)
                .between(PointsRecord::getCreateTime, begin, end);
        // 4.查询
        List<PointsRecord> list = getBaseMapper().queryUserPointsByDate(wrapper);
        if (CollUtils.isEmpty(list)) {
            return CollUtils.emptyList();
        }
        // 5.封装返回
        List<PointsStatisticsVO> vos = new ArrayList<>(list.size());
        for (PointsRecord p : list) {
            PointsStatisticsVO vo = new PointsStatisticsVO();
            vo.setType(p.getType().getDesc());
            vo.setMaxPoints(p.getType().getMaxPoints());
            vo.setPoints(p.getPoints());
            vos.add(vo);
        }
        return vos;
    }

    private int queryUserPointsByTypeAndDate(
            Long userId, PointsRecordType type, LocalDateTime begin, LocalDateTime end) {
        // 1.查询条件
        QueryWrapper<PointsRecord> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .eq(PointsRecord::getUserId, userId)
                .eq(type != null, PointsRecord::getType, type)
                .between(begin != null && end != null, PointsRecord::getCreateTime, begin, end);
        // 2.调用mapper，查询结果
        Integer points = getBaseMapper().queryUserPointsByTypeAndDate(wrapper);
        // 3.判断并返回
        return points == null ? 0 : points;
    }
}
