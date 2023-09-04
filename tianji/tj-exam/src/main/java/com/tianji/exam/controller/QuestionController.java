package com.tianji.exam.controller;


import com.tianji.api.dto.exam.QuestionDTO;
import com.tianji.common.domain.dto.PageDTO;
import com.tianji.exam.domain.dto.QuestionFormDTO;
import com.tianji.exam.domain.query.QuestionPageQuery;
import com.tianji.exam.domain.vo.QuestionDetailVO;
import com.tianji.exam.domain.vo.QuestionPageVO;
import com.tianji.exam.service.IQuestionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 题目 前端控制器
 * </p>
 *
 * @author 虎哥
 * @since 2022-09-02
 */
@Api(tags = "题目管理相关接口")
@RequiredArgsConstructor
@RestController
@RequestMapping("/questions")
public class QuestionController {

    private final IQuestionService questionService;

    @ApiOperation("新增题目")
    @PostMapping
    public void addQuestion(@Valid @RequestBody QuestionFormDTO questionDTO){
        questionService.addQuestion(questionDTO);
    }

    @ApiOperation("修改题目")
    @PutMapping("/{id}")
    public void updateQuestion(
            @ApiParam("要修改的题目的id") @PathVariable("id") Long id,
            @RequestBody QuestionFormDTO questionDTO){
        questionDTO.setId(id);
        questionService.updateQuestion(questionDTO);
    }

    @ApiOperation("删除题目")
    @DeleteMapping("/{id}")
    public void deleteQuestionById( @ApiParam("要删除的题目的id") @PathVariable("id") Long id){
        questionService.deleteQuestionById(id);
    }

    @ApiOperation("分页查询题目")
    @GetMapping("page")
    public PageDTO<QuestionPageVO> queryQuestionByPage(QuestionPageQuery query){
        return questionService.queryQuestionByPage(query);
    }

    @ApiOperation("查询题目详情")
    @GetMapping("{id}")
    public QuestionDetailVO queryQuestionDetailById(@ApiParam("要查询的题目的id") @PathVariable("id") Long id){
        return questionService.queryQuestionDetailById(id);
    }

    @ApiOperation("查询题目列表")
    @GetMapping("list")
    public List<QuestionDTO> queryQuestionByIds(@ApiParam("要查询的题目的id集合") @RequestParam("ids") List<Long> ids){
        return questionService.queryQuestionByIds(ids);
    }

    @ApiOperation("查询题目分值")
    @GetMapping("/scores")
    public Map<Long, Integer> queryQuestionScores(
            @ApiParam("要查询的题目的id集合") @RequestParam("ids") List<Long> ids){
        return questionService.queryQuestionScores(ids);
    }

    @ApiOperation("查询老师出题数量")
    @GetMapping("/numOfTeacher")
    public Map<Long, Integer> countSubjectNumOfTeacher(
            @ApiParam("要查询的老师的集合") @RequestParam("ids") List<Long> createrIds){
        return questionService.countQuestionNumOfCreater(createrIds);
    }

    @ApiOperation("查询业务关联的题目列表")
    @GetMapping("listOfBiz")
    public List<QuestionDTO> queryQuestionByBizId(@ApiParam("要查询的题目的id集合") @RequestParam("bizId") Long bizId){
        return questionService.queryQuestionByBizId(bizId);
    }

    @ApiOperation("校验名称是否有效，存在则无效返回false，不存在返回true")
    @GetMapping("/checkName")
    public Boolean checkNameValid(@RequestParam("name") String name){
        return questionService.checkNameValid(name);
    }
}
