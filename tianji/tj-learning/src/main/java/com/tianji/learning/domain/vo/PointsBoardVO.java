package com.tianji.learning.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(description = "积分榜单汇总信息")
public class PointsBoardVO {
    @ApiModelProperty("我的榜单排名")
    private Integer rank;
    @ApiModelProperty("我的积分值")
    private Integer points;
    @ApiModelProperty("前100名上榜人信息")
    private List<PointsBoardItemVO> boardList;
}
