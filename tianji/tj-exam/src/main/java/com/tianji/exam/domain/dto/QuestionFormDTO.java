package com.tianji.exam.domain.dto;

import com.tianji.common.validate.annotations.EnumValid;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 题目表单实体
 * </p>
 *
 * @author 虎哥
 * @since 2022-09-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(description = "考试题目表单实体")
public class QuestionFormDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("题目id，新增不用填写")
    private Long id;

    @ApiModelProperty("题目名称，题干")
    private String name;

    @ApiModelProperty("题目类型，1：单选题，2：多选题，3：不定向选择题，4：判断题，5：主观题")
    @NotNull(message = "题目类型为空，请设置题目类型")
    @EnumValid(enumeration = {1,2,3,4,5}, message = "题目类型错误")
    private Integer type;

    @ApiModelProperty("课程三级分类的id集合")
    private List<Long> cateIds;

    @ApiModelProperty("难易度，1：简单，2：中等，3：困难")
    @NotNull(message = "难度不能为空")
    @EnumValid(enumeration = {1,2,3},message = "题目难度错误")
    private Integer difficulty;

    @ApiModelProperty("分值")
    private Integer score;

    @ApiModelProperty("选择题选项，json数组格式")
    private List<String> options;

    @ApiModelProperty("选择题正确答案1到10，如果有多个答案，中间使用逗号隔开，如果是判断题，1：代表正确，其他代表错误")
    @NotNull(message = "题目答案不能为空")
    private String answer;

    @ApiModelProperty("答案解析")
    @Size(max = 300, min = 5, message = "答案解析长度为5-300")
    private String analysis;
}
