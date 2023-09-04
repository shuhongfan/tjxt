package com.tianji.trade.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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
public class PayChannelVO {

    @ApiModelProperty("支付渠道id")
    private Long id;
    @ApiModelProperty("支付渠道名称")
    private String name;
    @ApiModelProperty("支付渠道编码，唯一标示")
    private String channelCode;
    @ApiModelProperty("渠道优先级，数字越小优先级越高")
    private Integer channelPriority;
    @ApiModelProperty("渠道图标")
    private String channelIcon;
}
