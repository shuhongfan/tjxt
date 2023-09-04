package com.tianji.api.client.exam;

import com.tianji.api.dto.exam.QuestionBizDTO;
import com.tianji.api.dto.exam.QuestionDTO;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient("exam-service")
public interface ExamClient {

    @PostMapping("/question-biz/list")
    void saveQuestionBizInfoBatch(@RequestBody Iterable<QuestionBizDTO> qbs);

    @GetMapping("/question-biz/biz/list")
    List<QuestionBizDTO> queryQuestionIdsByBizIds(@RequestParam("ids") Iterable<Long> bizIds);

    @GetMapping("/question-biz/scores")
    Map<Long, Integer> queryQuestionScoresByBizIds(@RequestParam("ids") Iterable<Long> bizIds);

    @GetMapping("/questions/list")
    List<QuestionDTO> queryQuestionByIds(@ApiParam("要查询的题目的id集合") @RequestParam("ids") Iterable<Long> ids);

    @GetMapping("/questions/numOfTeacher")
    Map<Long, Integer> countSubjectNumOfTeacher(@RequestParam("ids") Iterable<Long> createrIds);

    @GetMapping("/questions//scores")
    Map<Long, Integer> queryQuestionScores(
            @ApiParam("要查询的题目的id集合") @RequestParam("ids") Iterable<Long> ids);
}
