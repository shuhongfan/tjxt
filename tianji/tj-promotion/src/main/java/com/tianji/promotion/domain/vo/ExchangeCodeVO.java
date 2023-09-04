package com.tianji.promotion.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@ApiModel(description = "兑换码实体")
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeCodeVO {
    @ApiModelProperty("兑换码id")
    private Integer id;
    @ApiModelProperty("兑换码")
    private String code;
}
