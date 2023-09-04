package com.tianji.message.domain.dto;

import com.tianji.common.domain.dto.BaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 第三方云通讯平台
 * </p>
 *
 * @author 虎哥
 * @since 2022-08-20
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(description = "短信第三方平台信息")
public class SmsThirdPlatformDTO extends BaseDTO {

    @ApiModelProperty("短信平台id，新增时无需填写")
    private Long id;
    @ApiModelProperty("短信平台名称")
    private String name;
    @ApiModelProperty("短信平台代码，例如：ali")
    private String code;
    @ApiModelProperty("数字越小优先级越高，最小为0")
    private Integer priority;
    @ApiModelProperty("短信平台状态：0-禁用，1-启用")
    private Integer status;
}
