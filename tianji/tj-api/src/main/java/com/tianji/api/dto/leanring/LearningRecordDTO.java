package com.tianji.api.dto.leanring;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "小节信息及学习进度")
public class LearningRecordDTO {
    @ApiModelProperty("对应节的id")
    private Long sectionId;
    @ApiModelProperty("视频的当前观看时长，单位秒")
    private Integer moment;
    @ApiModelProperty("是否完成学习，默认false")
    private Boolean finished;
}
