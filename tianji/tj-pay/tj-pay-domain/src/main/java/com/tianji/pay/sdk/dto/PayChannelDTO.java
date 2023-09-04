package com.tianji.pay.sdk.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * <p>
 * 支付渠道vo对象
 * </p>
 *
 * @author 虎哥
 * @since 2022-08-26
 */
@Data
@ApiModel(description = "支付渠道信息")
public class PayChannelDTO {

    @ApiModelProperty("支付渠道id")
    private Long id;
    @NotNull(message = "渠道名称不能为空")
    @Length(min = 1, max = 50, message = "渠道名称过长")
    @ApiModelProperty("支付渠道名称")
    private String name;
    @NotNull(message = "渠道编码不能为空")
    @Pattern(regexp = "\\w{1,30}", message = "渠道编码格式错误")
    @ApiModelProperty("支付渠道编码，唯一标示")
    private String channelCode;
    @NotNull(message = "渠道优先级不能为空")
    @ApiModelProperty("渠道优先级，数字越小优先级越高")
    private Integer channelPriority;
    @NotNull(message = "渠道图标不能为空")
    @Length(min = 1, max = 255, message = "渠道图标地址过长")
    @ApiModelProperty("渠道图标")
    private String channelIcon;
    @ApiModelProperty("支付渠道状态，1：使用中，2：停用。新增时默认为1")
    private Integer status;
}
