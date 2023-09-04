package com.tianji.course.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 题目数据
 * @author wusongsong
 * @since 2022/7/11 20:24
 * @version 1.0.0
 **/
@Data
@ApiModel(description = "题目分页数据")
public class SubjectVO {
    @ApiModelProperty("题目id")
    private Long id;
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("所属分类,每一个分类三级分类中间使用/分开")
    private List<String> cates;
    @ApiModelProperty("题目类型")
    private Integer subjectType;
    @ApiModelProperty("题目类型描述")
    private String subjectTypeDesc;
    @ApiModelProperty("题目难易度描述")
    private String difficultDesc;
    @ApiModelProperty("难易度,1：简单，2：中等，3：困难")
    private String difficulty;
    @ApiModelProperty("分值")
    private Integer score;
    @ApiModelProperty("使用次数")
    private Integer useTimes;
    @ApiModelProperty("作答次数")
    private Integer answerTimes;
    @ApiModelProperty("更新人")
    private String updaterName;
    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
    @ApiModelProperty("选项")
    private List<String> options;
    @ApiModelProperty("答案,判断题，数组第一个如果是1，代表正确，其他代表错误")
    private List<Integer> answers;
    @ApiModelProperty("解析")
    private String analysis;
    @ApiModelProperty("正确率，百分号精确到小数点后一位")
    private String accuRate;
}
