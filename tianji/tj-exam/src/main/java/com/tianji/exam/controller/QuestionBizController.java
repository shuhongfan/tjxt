package com.tianji.exam.controller;


import com.tianji.api.dto.exam.QuestionBizDTO;
import com.tianji.exam.service.IQuestionBizService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 问题和业务关联表，例如把小节id和问题id关联，一个小节下可以有多个问题 前端控制器
 * </p>
 *
 * @author 虎哥
 * @since 2022-09-02
 */
@Api(tags = "题目管理相关接口")
@RestController
@RequestMapping("/question-biz")
@RequiredArgsConstructor
public class QuestionBizController {

    private final IQuestionBizService bizService;

    @ApiOperation("批量保存题目和业务关系")
    @PostMapping("list")
    public void saveQuestionBizInfoBatch(@RequestBody List<QuestionBizDTO> qbs){
        bizService.saveQuestionBizInfoBatch(qbs);
    }

    @ApiOperation("查询与业务有关的题目id")
    @GetMapping("/biz/{id}")
    public List<QuestionBizDTO> queryQuestionIdsByBizId(@ApiParam("业务id") @PathVariable("id") Long bizId){
        return bizService.queryQuestionIdsByBizId(bizId);
    }

    @ApiOperation("批量查询与业务有关的题目id")
    @GetMapping("/biz/list")
    public List<QuestionBizDTO> queryQuestionIdsByBizIds(@ApiParam("业务id集合") @RequestParam("ids") List<Long> bizIds){
        return bizService.queryQuestionIdsByBizIds(bizIds);
    }

    @ApiOperation("查询业务下的题目分数和")
    @GetMapping("/scores")
    public Map<Long, Integer> queryQuestionScoresByBizIds(@RequestParam("ids") List<Long> bizIds){
        return bizService.queryQuestionScoresByBizIds(bizIds);
    }
}
