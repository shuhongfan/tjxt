package com.tianji.trade.domain.dto;

import com.tianji.common.validate.annotations.EnumValid;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(description = "退款审批模型")
public class ApproveFormDTO{
    @ApiModelProperty("退款id")
    @NotNull(message = "退款id不能为空")
    private Long id;
    @NotNull(message = "审批类型不能为空")
    @EnumValid(enumeration = {1,2}, message = "审批只有同意和拒绝两种操作")
    @ApiModelProperty("审批类型，1：同意，2：拒绝")
    public Integer approveType;
    @ApiModelProperty("审批意见")
    private String approveOpinion;
    @ApiModelProperty("备注")
    private String remark;
}