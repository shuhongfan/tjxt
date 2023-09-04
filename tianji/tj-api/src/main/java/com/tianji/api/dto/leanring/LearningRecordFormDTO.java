package com.tianji.api.dto.leanring;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel(description = "学习记录表单数据")
public class LearningRecordFormDTO {

    @ApiModelProperty("小节类型：1-视频，2-考试")
    private Integer sectionType;

    @ApiModelProperty("课表id")
    private Long lessonId;

    @ApiModelProperty("对应节的id")
    private Long sectionId;

    @ApiModelProperty("视频总时长，单位秒")
    private Integer duration;

    @ApiModelProperty("视频的当前观看时长，单位秒，第一次提交填0")
    private Integer moment;

    @ApiModelProperty("提交时间")
    private LocalDateTime commitTime;
}