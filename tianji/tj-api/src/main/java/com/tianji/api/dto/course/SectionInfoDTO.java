package com.tianji.api.dto.course;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@ApiModel("小节信息，包含课程id和媒资id")
@AllArgsConstructor
@NoArgsConstructor
public class SectionInfoDTO {
    @ApiModelProperty("课程id")
    private Long courseId;
    @ApiModelProperty("媒资id")
    private Long mediaId;
    @ApiModelProperty("是否支持免费试看")
    private Boolean trailer;
    @ApiModelProperty("免费时长，不免费为0，单位分钟")
    private Integer freeDuration;
}
