package com.tianji.learning.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel(description = "用户端互动问题信息")
public class QuestionAdminVO {
    @ApiModelProperty("主键id")
    private Long id;
    @ApiModelProperty("互动问题名称")
    private String title;
    @ApiModelProperty("互动问题描述")
    private String description;
    @ApiModelProperty("回答数量，0表示没有回答")
    private Integer answerTimes;
    @ApiModelProperty(value = "创建时间", example = "2022-7-18 19:52:36")
    private LocalDateTime createTime;
    @ApiModelProperty("管理端问题状态：0-未查看，1-已查看")
    private Integer status;
    @ApiModelProperty("是否被隐藏")
    private Boolean hidden;

    @ApiModelProperty("提问者昵称")
    private String userName;
    @ApiModelProperty("提问者头像")
    private String userIcon;
    @ApiModelProperty("课程名称")
    private String courseName;
    @ApiModelProperty("教师名称")
    private String teacherName;
    @ApiModelProperty("章名称")
    private String chapterName;
    @ApiModelProperty("节名称")
    private String sectionName;
    @ApiModelProperty("三级分类名称，中间使用/隔开")
    private String categoryName;
}















