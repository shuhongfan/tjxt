package com.tianji.learning.domain.vo;

import com.tianji.learning.enums.LessonStatus;
import com.tianji.learning.enums.PlanStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel(description = "课程表信息")
public class LearningLessonVO {

    @ApiModelProperty("主键lessonId")
    private Long id;

    @ApiModelProperty("课程id")
    private Long courseId;

    @ApiModelProperty("课程名称")
    private String courseName;

    @ApiModelProperty("课程封面")
    private String courseCoverUrl;

    @ApiModelProperty("课程章节数量")
    private Integer sections;

    @ApiModelProperty("课程状态，0-未学习，1-学习中，2-已学完，3-已失效")
    private LessonStatus status;

    @ApiModelProperty("总已学习章节数")
    private Integer learnedSections;

    @ApiModelProperty("总已报名课程数")
    private Integer courseAmount;

    @ApiModelProperty("课程购买时间")
    private LocalDateTime createTime;

    @ApiModelProperty("课程过期时间，如果为null代表课程永久有效")
    private LocalDateTime expireTime;

    @ApiModelProperty("习计划状态，0-没有计划，1-计划进行中")
    private PlanStatus planStatus;

    @ApiModelProperty("计划的学习频率")
    private Integer weekFreq;

    @ApiModelProperty("最近学习的小节名")
    private String latestSectionName;

    @ApiModelProperty("最近学习的小节编号")
    private Integer latestSectionIndex;
}
