package com.tianji.exam.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tianji.api.dto.IdAndNumDTO;
import com.tianji.exam.domain.po.QuestionBiz;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 问题和业务关联表，例如把小节id和问题id关联，一个小节下可以有多个问题 Mapper 接口
 * </p>
 *
 * @author 虎哥
 * @since 2022-09-02
 */
public interface QuestionBizMapper extends BaseMapper<QuestionBiz> {

    List<IdAndNumDTO> countQuestionScoresByBizIds(@Param("bizIds") Iterable<Long> bizIds);

    List<IdAndNumDTO> countUsedTimes(@Param("qIds") Iterable<Long> qIds);
}
