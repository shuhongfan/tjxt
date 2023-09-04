package com.tianji.promotion.domain.dto;

import com.tianji.common.validate.annotations.EnumValid;
import com.tianji.promotion.constants.ScopeType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@ApiModel(description = "优惠券使用范围")
public class CouponScopeDTO {
    @ApiModelProperty("范围类型，0-不限范围，1-限定分类，2-限定课程")
    @EnumValid(enumeration = {0, 1, 2}, message = "范围类型错误")
    private ScopeType scopeType;
    @ApiModelProperty("范围id集合")
    private List<Long> scopeIds;
    @ApiModelProperty("范围名称集合，新增时无需添加")
    private List<String> scopeNames;
}
