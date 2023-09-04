package com.tianji.learning.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "每日积分统计实体")
public class PointsStatisticsVO {
    @ApiModelProperty("获取积分方式")
    private String type;
    @ApiModelProperty("今日已获取积分值")
    private Integer points;
    @ApiModelProperty("单日积分上限")
    private Integer maxPoints;
}
