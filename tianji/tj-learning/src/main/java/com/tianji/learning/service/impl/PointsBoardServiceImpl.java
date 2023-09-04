package com.tianji.learning.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tianji.api.client.user.UserClient;
import com.tianji.api.dto.user.UserDTO;
import com.tianji.common.utils.CollUtils;
import com.tianji.common.utils.DateUtils;
import com.tianji.common.utils.UserContext;
import com.tianji.learning.constants.RedisConstants;
import com.tianji.learning.domain.po.PointsBoard;
import com.tianji.learning.domain.query.PointsBoardQuery;
import com.tianji.learning.domain.vo.PointsBoardItemVO;
import com.tianji.learning.domain.vo.PointsBoardVO;
import com.tianji.learning.mapper.PointsBoardMapper;
import com.tianji.learning.service.IPointsBoardService;
import com.tianji.learning.utils.TableInfoContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.BoundZSetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.tianji.learning.constants.LearningConstants.POINTS_BOARD_TABLE_PREFIX;

/**
 * <p>
 * 学霸天梯榜 服务实现类
 * </p>
 *
 * @author 虎哥
 */
@Service
@RequiredArgsConstructor
public class PointsBoardServiceImpl extends ServiceImpl<PointsBoardMapper, PointsBoard> implements IPointsBoardService {

    private final StringRedisTemplate redisTemplate;

    private final UserClient userClient;

    @Override
    public PointsBoardVO queryPointsBoardBySeason(PointsBoardQuery query) {
        // 1.判断是否是查询当前赛季
        Long season = query.getSeason();
        boolean isCurrent = season == null || season == 0;
        // 2.获取Redis的Key
        LocalDateTime now = LocalDateTime.now();
        String key = RedisConstants.POINTS_BOARD_KEY_PREFIX + now.format(DateUtils.POINTS_BOARD_SUFFIX_FORMATTER);
        // 2.查询我的积分和排名
        PointsBoard myBoard = isCurrent ?
                queryMyCurrentBoard(key) : // 查询当前榜单（Redis）
                queryMyHistoryBoard(season); // 查询历史榜单（MySQL）
        // 3.查询榜单列表
        List<PointsBoard> list = isCurrent ?
                queryCurrentBoardList(key, query.getPageNo(), query.getPageSize()) :
                queryHistoryBoardList(query);
        // 4.封装VO
        PointsBoardVO vo = new PointsBoardVO();
        // 4.1.处理我的信息
        if (myBoard != null) {
            vo.setPoints(myBoard.getPoints());
            vo.setRank(myBoard.getRank());
        }
        if (CollUtils.isEmpty(list)) {
            return vo;
        }
        // 4.2.查询用户信息
        Set<Long> uIds = list.stream().map(PointsBoard::getUserId).collect(Collectors.toSet());
        List<UserDTO> users = userClient.queryUserByIds(uIds);
        Map<Long, String> userMap = new HashMap<>(uIds.size());
        if(CollUtils.isNotEmpty(users)) {
            userMap = users.stream().collect(Collectors.toMap(UserDTO::getId, UserDTO::getName));
        }
        // 4.3.转换VO
        List<PointsBoardItemVO> items = new ArrayList<>(list.size());
        for (PointsBoard p : list) {
            PointsBoardItemVO v = new PointsBoardItemVO();
            v.setPoints(p.getPoints());
            v.setRank(p.getRank());
            v.setName(userMap.get(p.getUserId()));
            items.add(v);
        }
        vo.setBoardList(items);
        return vo;
    }

    @Override
    public void createPointsBoardTableBySeason(Integer season) {
        getBaseMapper().createPointsBoardTable(POINTS_BOARD_TABLE_PREFIX + season);
    }

    private List<PointsBoard> queryHistoryBoardList(PointsBoardQuery query) {
        // 1.计算表名
        TableInfoContext.setInfo(POINTS_BOARD_TABLE_PREFIX + query.getSeason());
        // 2.查询数据
        Page<PointsBoard> page = page(query.toMpPage());
        // 3.数据处理
        List<PointsBoard> records = page.getRecords();
        if (CollUtils.isEmpty(records)) {
            return CollUtils.emptyList();
        }
        records.forEach(b -> b.setRank(b.getId().intValue()));
        return records;
    }

    @Override
    public List<PointsBoard> queryCurrentBoardList(String key, Integer pageNo, Integer pageSize) {
        // 1.计算分页
        int from = (pageNo - 1) * pageSize;
        // 2.查询
        Set<ZSetOperations.TypedTuple<String>> tuples = redisTemplate.opsForZSet()
                .reverseRangeWithScores(key, from, from + pageSize - 1);
        if (CollUtils.isEmpty(tuples)) {
            return CollUtils.emptyList();
        }
        // 3.封装
        int rank = from + 1;
        List<PointsBoard> list = new ArrayList<>(tuples.size());
        for (ZSetOperations.TypedTuple<String> tuple : tuples) {
            String userId = tuple.getValue();
            Double points = tuple.getScore();
            if (userId == null || points == null) {
                continue;
            }
            PointsBoard p = new PointsBoard();
            p.setUserId(Long.valueOf(userId));
            p.setPoints(points.intValue());
            p.setRank(rank++);
            list.add(p);
        }
        return list;
    }

    private PointsBoard queryMyHistoryBoard(Long season) {
        // 1.获取登录用户
        Long userId = UserContext.getUser();
        // 2.计算表名
        TableInfoContext.setInfo(POINTS_BOARD_TABLE_PREFIX + season);
        // 3.查询数据
        Optional<PointsBoard> opt = lambdaQuery().eq(PointsBoard::getUserId, userId).oneOpt();
        if (opt.isEmpty()) {
            return null;
        }
        // 4.转换数据
        PointsBoard pointsBoard = opt.get();
        pointsBoard.setRank(pointsBoard.getId().intValue());
        return pointsBoard;
    }

    private PointsBoard queryMyCurrentBoard(String key) {
        // 1.绑定key
        BoundZSetOperations<String, String> ops = redisTemplate.boundZSetOps(key);
        // 2.获取当前用户信息
        String userId = UserContext.getUser().toString();
        // 3.查询积分
        Double points = ops.score(userId);
        // 4.查询排名
        Long rank = ops.reverseRank(userId);
        // 5.封装返回
        PointsBoard p = new PointsBoard();
        p.setPoints(points == null ? 0 : points.intValue());
        p.setRank(rank == null ? 0 : rank.intValue() + 1);
        return p;
    }
}
