package com.tianji.course.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 题目详情
 *
 * @author wusongsong
 * @since 2022/7/11 20:54
 * @version 1.0.0
 **/
@Data
@ApiModel(description = "题目详情")
public class SubjectInfoVO {
    @ApiModelProperty("题目id")
    private Long id;
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("所属题目分类")
    private List<CateSimpleInfoVO> cates;
    @ApiModelProperty("题目类型")
    private Integer subjectType;
    @ApiModelProperty("题目难易度")
    private Integer difficulty;
    @ApiModelProperty("分值")
    private Integer score;
    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
    @ApiModelProperty("更新人")
    private String updaterName;
    @ApiModelProperty("课程名称信息")
    private List<CourseSimpleInfoVO> courses;

    @ApiModelProperty("选项")
    private List<String> options;
    @ApiModelProperty("答案,判断题，数组第一个如果是1，代表正确，其他代表错误")
    private List<Integer> answers;
    @ApiModelProperty("解析")
    private String analysis;
    @ApiModelProperty("课程id列表")
    private List<Long> courseIds;
    @ApiModelProperty(value = "被引用次数", example = "10")
    private Integer useTimes;
    @ApiModelProperty("作答次数")
    private Integer answerTimes;
    @ApiModelProperty("正确率")
    private Double correctRate;


}
