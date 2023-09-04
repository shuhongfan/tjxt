package com.tianji.course.domain.dto;

import com.tianji.common.exceptions.BadRequestException;
import com.tianji.common.exceptions.BizIllegalException;
import com.tianji.common.utils.CollUtils;
import com.tianji.common.utils.StringUtils;
import com.tianji.common.validate.Checker;
import com.tianji.common.validate.annotations.EnumValid;
import com.tianji.course.constants.SubjectConstants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 题目保存模型
 * @author wusongsong
 * @since 2022/7/11 21:10
 * @version 1.0.0
 **/
@ApiModel("题目保存模型")
@Data
public class SubjectSaveDTO implements Checker {
    @ApiModelProperty("题目id，为空新增，不为空更新")
    private Long id;
    @ApiModelProperty("名称")
    @NotNull(message = "题目为空，请设置题目")
    @Size(max = 200, min = 5, message = "题目长度为5-200")
    private String name;
    @ApiModelProperty("所属题目分类")
    @NotNull(message = "题目分类为空，请设置题目分类")
    private List<List<Long>> cates;
    @ApiModelProperty("题目类型")
    @NotNull(message = "题目类型为空，请设置题目类型")
    @EnumValid(enumeration = {1,2,3,4,5}, message = "题目类型只有单选题，多选题，不定向选择题，判断题，您的题目超出题纲")
    private Integer subjectType;
    @ApiModelProperty("题目难易度")
    @NotNull(message = "难度不能为空")
    @EnumValid(enumeration = {1,2,3},message = "题目难度只有简单，中等，困难")
    private Integer difficulty;
    @ApiModelProperty("分值")
    private Integer score;

    @ApiModelProperty("课程id")
    private List<Long> courseIds;

    @ApiModelProperty("选项,最多10个")
    private List<String> options;

    @ApiModelProperty("答案,判断题，数组第一个如果是1，代表正确，其他代表错误")
    @NotNull(message = "题目答案不能为空")
    private List<Integer> answers;
    @ApiModelProperty("解析")
    private String analysis;

    @Override
    public void check() {
        //选择题 单选，多选，不定向选择
        if(subjectType == SubjectConstants.Type.SIGNLE_CHOICE.getType() ||
                subjectType == SubjectConstants.Type.MUtiple_CHOICE.getType() ||
                subjectType == SubjectConstants.Type.NON_DIRECTIONAL_CHOICE.getType()){
            Integer answerOptionMax = answers.stream().max(Integer::compare).get();
            //选项最少1个最多10个
            if(CollUtils.isEmpty(options) || options.size() > 10){
                throw new BizIllegalException("最少1个选项，最多10个选项");
            }
            //选择题答案 不能超过选项数
            if(answerOptionMax > options.size()){
                throw new BizIllegalException("存在正确的答案找不到选项");
            }
            if(StringUtils.isNotEmpty(analysis)
                    && (StringUtils.length(analysis) < 5
                    || StringUtils.length(analysis) > 300)) {
                throw new BadRequestException("答案解析长度为5-300");
            }
        }

    }
}
