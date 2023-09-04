package com.tianji.promotion.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@ApiModel(description = "优惠券使用范围")
@NoArgsConstructor
@AllArgsConstructor
public class CouponScopeVO {
    @ApiModelProperty("范围id集合")
    private Long id;
    @ApiModelProperty("范围名称集合")
    private String name;
}
