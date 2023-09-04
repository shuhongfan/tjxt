package com.tianji.course.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @ClassName SubjectPageParamDTO
 * @Author wusongsong
 * @Date 2022/7/11 20:07
 * @Version
 **/
@Data
@ApiModel(description = "题目分页参数")
public class SubjectPageParamDTO {
    @ApiModelProperty("一级课程分类")
    private Long firstCateId;
    @ApiModelProperty("二级课程分类")
    private Long secondCateId;
    @ApiModelProperty("三级课程分类id列表")
    private List<Long> thirdCateIds;
    @ApiModelProperty("难易度,1：简单，2：中等，3：困难")
    private Integer difficulty;
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("题目类型，1：单选题，2：多选题，3：不定向选择题，4：判断题，多选中间逗号隔开,不传表示全选")
    private String subjectTypes;
    @ApiModelProperty("是否全选当前用户，默认搜索所有")
    private Boolean own;
}
