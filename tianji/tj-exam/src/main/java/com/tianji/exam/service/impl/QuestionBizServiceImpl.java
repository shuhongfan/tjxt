package com.tianji.exam.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tianji.api.dto.IdAndNumDTO;
import com.tianji.api.dto.exam.QuestionBizDTO;
import com.tianji.common.utils.BeanUtils;
import com.tianji.common.utils.CollUtils;
import com.tianji.exam.domain.po.QuestionBiz;
import com.tianji.exam.mapper.QuestionBizMapper;
import com.tianji.exam.service.IQuestionBizService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 问题和业务关联表，例如把小节id和问题id关联，一个小节下可以有多个问题 服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2022-09-02
 */
@Service
public class QuestionBizServiceImpl extends ServiceImpl<QuestionBizMapper, QuestionBiz> implements IQuestionBizService {

    @Override
    public int countUsedTimes(Long questionId) {
        Integer count = lambdaQuery()
                .eq(QuestionBiz::getQuestionId, questionId)
                .count();
        return count == null ? 0 : count;
    }

    @Override
    public Map<Long, Integer> countUsedTimes(Set<Long> qIds) {
        // 1.统计引用次数
        List<IdAndNumDTO> list = baseMapper.countUsedTimes(qIds);
        // 2.转换返回
        return IdAndNumDTO.toMap(list);
    }

    @Override
    public List<QuestionBizDTO> queryQuestionIdsByBizId(Long bizId) {
        List<QuestionBiz> list = lambdaQuery()
                .eq(QuestionBiz::getBizId, bizId)
                .list();
        return BeanUtils.copyList(list, QuestionBizDTO.class);
    }

    @Override
    public List<QuestionBizDTO> queryQuestionIdsByBizIds(List<Long> bizIds) {
        if (CollUtils.isEmpty(bizIds)) {
            return CollUtils.emptyList();
        }
        List<QuestionBiz> list = lambdaQuery()
                .in(QuestionBiz::getBizId, bizIds)
                .list();
        return BeanUtils.copyList(list, QuestionBizDTO.class);
    }

    @Override
    @Transactional
    public void saveQuestionBizInfoBatch(List<QuestionBizDTO> qbs) {
        if (CollUtils.isEmpty(qbs)) {
            return;
        }
        // 1.获取业务id
        Set<Long> bizIds = qbs.stream().map(QuestionBizDTO::getBizId).collect(Collectors.toSet());
        // 2.删除旧数据
        remove(new LambdaQueryWrapper<QuestionBiz>().in(QuestionBiz::getBizId, bizIds));
        // 3.插入新数据
        List<QuestionBiz> list = qbs.stream()
                .map(q -> QuestionBiz.of(null, q.getBizId(), q.getQuestionId()))
                .collect(Collectors.toList());
        saveBatch(list);
    }

    @Override
    public Map<Long, Integer> queryQuestionScoresByBizIds(Iterable<Long> bizIds) {
        if (CollUtils.isEmpty(bizIds)) {
            return CollUtils.emptyMap();
        }
        // 1.统计biz及对应题目的分数和
        List<IdAndNumDTO> list = baseMapper.countQuestionScoresByBizIds(bizIds);
        // 2.数据处理
        return IdAndNumDTO.toMap(list);
    }
}
