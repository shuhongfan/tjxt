package com.tianji.exam.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(description = "考试详情数据")
public class QuestionDetailVO {

    @ApiModelProperty("题目id")
    private Long id;

    @ApiModelProperty("题目名称，题干")
    private String name;

    @ApiModelProperty("题目类型，1：单选题，2：多选题，3：不定向选择题，4：判断题，5：主观题")
    private Integer type;

    @ApiModelProperty("1级课程分类id")
    private Long cateId1;

    @ApiModelProperty("2级课程分类id")
    private Long cateId2;

    @ApiModelProperty("3级课程分类id")
    private Long cateId3;
    @ApiModelProperty("课程三级分类的名称集合")
    private List<String> categories;

    @ApiModelProperty("难易度，1：简单，2：中等，3：困难")
    private Integer difficulty;

    @ApiModelProperty("分值")
    private Integer score;

    @ApiModelProperty("引用次数")
    private Integer useTimes;

    @ApiModelProperty("回答正确次数")
    private Integer correctTimes;

    @ApiModelProperty("回答次数")
    private Integer answerTimes;

    @ApiModelProperty("更新人")
    private String updater;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty("选择题选项，json数组格式")
    private List<String> options;

    @ApiModelProperty("选择题正确答案1到10，如果有多个答案，中间使用逗号隔开，如果是判断题，1：代表正确，其他代表错误")
    private String answer;

    @ApiModelProperty("答案解析")
    private String analysis;
}
