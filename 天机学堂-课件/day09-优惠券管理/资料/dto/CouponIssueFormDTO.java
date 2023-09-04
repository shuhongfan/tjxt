package com.tianji.promotion.domain.dto;

import com.tianji.common.utils.DateUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@ApiModel(description = "优惠券发放的表单实体")
public class CouponIssueFormDTO {
    @ApiModelProperty("优惠券id")
    private Long id;
    @ApiModelProperty("发放开始时间")
    @DateTimeFormat(pattern = DateUtils.DEFAULT_DATE_TIME_FORMAT)
    @Future(message = "发放开始时间必须晚于当前时间")
    private LocalDateTime issueBeginTime;
    @ApiModelProperty("发放结束时间")
    @Future(message = "发放结束时间必须晚于当前时间")
    @NotNull(message = "发放结束时间不能为空")
    @DateTimeFormat(pattern = DateUtils.DEFAULT_DATE_TIME_FORMAT)
    private LocalDateTime issueEndTime;


    @ApiModelProperty("有效天数")
    private Integer termDays;
    @ApiModelProperty("使用有效期开始时间")
    @DateTimeFormat(pattern = DateUtils.DEFAULT_DATE_TIME_FORMAT)
    private LocalDateTime termBeginTime;
    @ApiModelProperty("使用有效期结束时间")
    @DateTimeFormat(pattern = DateUtils.DEFAULT_DATE_TIME_FORMAT)
    private LocalDateTime termEndTime;
}
