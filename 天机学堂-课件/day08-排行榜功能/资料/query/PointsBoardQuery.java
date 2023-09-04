package com.tianji.learning.domain.query;

import com.tianji.common.domain.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(description = "积分排行榜分页查询条件")
public class PointsBoardQuery extends PageQuery {
    @ApiModelProperty(value = "赛季id，为null或者0则代表查询当前赛季")
    private Long season;
}
