package com.tianji.promotion.domain.vo;

import com.tianji.promotion.enums.CouponStatus;
import com.tianji.promotion.enums.DiscountType;
import com.tianji.promotion.enums.ObtainType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel(description = "优惠券分页数据")
public class CouponPageVO {
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

    @ApiModelProperty("获取方式1：手动领取，2：指定发放（通过兑换码兑换）")
    private ObtainType obtainWay;
    @ApiModelProperty("已使用")
    private Integer usedNum;
    @ApiModelProperty("已发放数量")
    private Integer issueNum;
    @ApiModelProperty("优惠券总量")
    private Integer totalNum;

    @ApiModelProperty("优惠券创建时间")
    private LocalDateTime createTime;
    @ApiModelProperty("发放开始时间")
    private LocalDateTime issueBeginTime;
    @ApiModelProperty("发放结束时间")
    private LocalDateTime issueEndTime;

    @ApiModelProperty("有效天数")
    private Integer termDays;
    @ApiModelProperty("使用有效期开始时间")
    private LocalDateTime termBeginTime;
    @ApiModelProperty("使用有效期结束时间")
    private LocalDateTime termEndTime;

    @ApiModelProperty("状态")
    private CouponStatus status;
}
