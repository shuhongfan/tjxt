package com.tianji.course.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 课程视频保存模型
 * @author wusongsong
 * @since 2022/7/13 15:09
 * @version 1.0.0
 **/
@Data
@ApiModel(description = "课程视频保存模型")
public class CourseMediaSaveDTO {
    @ApiModelProperty("小节id")
    private Long cataId;
    @ApiModelProperty("媒资id")
    private Long mediaId;
    @ApiModelProperty("是否支持试看")
    private Boolean trailer;
}
