package com.tianji.trade.domain.query;

import com.tianji.common.domain.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(description = "订单分页查询条件")
public class OrderPageQuery extends PageQuery {
    @ApiModelProperty("订单状态")
    private Integer status;
}
