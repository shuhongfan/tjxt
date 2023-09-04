package com.tianji.api.dto.course;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 考试记录表
 * </p>
 *
 * @author 虎哥
 * @since 2022-07-18
 */
@Data
@ApiModel(description = "考试问题详情")
public class SubjectDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("问题id")
    private Long id;

    @ApiModelProperty("题干")
    private String name;

    @ApiModelProperty("选择题的选项")
    private List<String> options;

    @ApiModelProperty("分值")
    private Integer score;

    @ApiModelProperty("问题类型，1：单选题，2：多选题，3：不定向选择题，4：判断题，5：主观题")
    private Integer subjectType;

    @ApiModelProperty("难易度，1：简单，2：中等，3：困难")
    private Integer difficulty;

    @ApiModelProperty("解析")
    private String analysis;

    @ApiModelProperty("选择题答案，0对应A，1对应B，可填多个")
    private List<Integer> answers;
}
