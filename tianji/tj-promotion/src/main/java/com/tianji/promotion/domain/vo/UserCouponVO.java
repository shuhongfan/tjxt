package com.tianji.promotion.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@ApiModel(description = "用户优惠券")
public class UserCouponVO{

    @ApiModelProperty("优惠券id")
    private Long id;

    @ApiModelProperty("优惠券名称")
    private String name;

    @ApiModelProperty("是否限定范围, false: 无限制， true：限定范围")
    private Boolean specify;

    @ApiModelProperty("折扣规则")
    private String rule;

    @ApiModelProperty("优惠金额")
    private Integer discountValue;

    @ApiModelProperty("优惠类型，type：1：每满减，2：折扣 3：无门槛，4：普通满减")
    private Integer discountType;

    @ApiModelProperty("优惠券有效期截止时间")
    private LocalDateTime termEndTime;
}
