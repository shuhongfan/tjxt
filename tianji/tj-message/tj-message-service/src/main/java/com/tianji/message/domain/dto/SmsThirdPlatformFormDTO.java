package com.tianji.message.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 第三方云通讯平台
 * </p>
 *
 * @author 虎哥
 * @since 2022-08-20
 */
@Data
@ApiModel(description = "短信第三方平台信息的表单实体")
public class SmsThirdPlatformFormDTO {

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
