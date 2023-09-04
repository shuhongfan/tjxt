package com.tianji.exam.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tianji.api.dto.exam.QuestionBizDTO;
import com.tianji.exam.domain.po.QuestionBiz;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 问题和业务关联表，例如把小节id和问题id关联，一个小节下可以有多个问题 服务类
 * </p>
 *
 * @author 虎哥
 * @since 2022-09-02
 */
public interface IQuestionBizService extends IService<QuestionBiz> {

    int countUsedTimes(Long questionId);

    Map<Long, Integer> countUsedTimes(Set<Long> qIds);

    List<QuestionBizDTO> queryQuestionIdsByBizId(Long bizId);

    List<QuestionBizDTO> queryQuestionIdsByBizIds(List<Long> bizIds);

    void saveQuestionBizInfoBatch(List<QuestionBizDTO> qbs);

    Map<Long, Integer> queryQuestionScoresByBizIds(Iterable<Long> bizIds);
}
