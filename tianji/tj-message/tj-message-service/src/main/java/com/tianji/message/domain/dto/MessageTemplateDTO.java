package com.tianji.message.domain.dto;

import com.tianji.common.domain.dto.BaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 第三方短信平台
 * </p>
 *
 * @author 虎哥
 * @since 2022-08-19
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(description = "短信模板")
public class MessageTemplateDTO extends BaseDTO {

    @ApiModelProperty("信发送模板id，新增时无需填写")
    private Long id;

    @ApiModelProperty("第三方短信推送渠道id")
    private Long platformCode;

    @ApiModelProperty("第三方短信推送渠道名称")
    private String platformName;

    @ApiModelProperty("短信模板名称")
    private String name;

    @ApiModelProperty("短信模板预览内容")
    private String content;

    @ApiModelProperty("第三方平台短信签名")
    private String signName;

    @ApiModelProperty("第三方平台短信模板code")
    private String thirdTemplateCode;

    @ApiModelProperty("模板状态:  0-草稿，1-使用中，2-停用")
    private Integer status;
}
