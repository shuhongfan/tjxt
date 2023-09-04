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
@ApiModel(description = "考试题目分页数据")
public class QuestionPageVO {

    @ApiModelProperty("题目id")
    private Long id;

    @ApiModelProperty("题目名称，题干")
    private String name;

    @ApiModelProperty("题目类型，1：单选题，2：多选题，3：不定向选择题，4：判断题，5：主观题")
    private Integer type;

    @ApiModelProperty("课程三级分类的名称集合")
    private List<String> categories;

    @ApiModelProperty("难易度，1：简单，2：中等，3：困难")
    private Integer difficulty;

    @ApiModelProperty("分值")
    private Integer score;

    @ApiModelProperty("引用次数")
    private Integer useTimes;

    @ApiModelProperty("回答次数")
    private Integer answerTimes;

    @ApiModelProperty("更新人")
    private String updater;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
}
