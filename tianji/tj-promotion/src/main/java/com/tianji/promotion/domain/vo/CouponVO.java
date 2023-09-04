package com.tianji.promotion.domain.vo;

import com.tianji.promotion.enums.DiscountType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel(description = "用户端优惠券信息")
public class CouponVO {
    @ApiModelProperty("优惠券id，新增不需要添加，更新必填")
    private Long id;
    @ApiModelProperty("优惠券名称")
    private String name;
    @ApiModelProperty("是否限定使用范围")
    private Boolean specific;

    @ApiModelProperty("优惠券类型，1：每满减，2：折扣，3：无门槛，4：普通满减")
    private DiscountType discountType;
    @ApiModelProperty("折扣门槛，0代表无门槛")
    private Integer thresholdAmount;
    @ApiModelProperty("折扣值，满减填抵扣金额；打折填折扣值：80标示打8折")
    private Integer discountValue;
    @ApiModelProperty("最大优惠金额")
    private Integer maxDiscountAmount;

    @ApiModelProperty("有效天数")
    private Integer termDays;
    @ApiModelProperty("使用有效期结束时间")
    private LocalDateTime termEndTime;

    @ApiModelProperty("是否可以领取")
    private Boolean available;

    @ApiModelProperty("是否可以使用")
    private Boolean received;
}
