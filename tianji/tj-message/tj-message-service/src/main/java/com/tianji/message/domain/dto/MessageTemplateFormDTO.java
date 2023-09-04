package com.tianji.message.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 第三方短信平台
 * </p>
 *
 * @author 虎哥
 * @since 2022-08-19
 */
@Data
@ApiModel(description = "短信模板表单实体")
public class MessageTemplateFormDTO {

    @ApiModelProperty("信发送模板id，新增时无需填写")
    private Long id;

    @ApiModelProperty("第三方短信平台代号")
    private String platformCode;

    @ApiModelProperty("第三方平台短信签名")
    private String signName;

    @ApiModelProperty("第三方平台短信模板code")
    private String thirdTemplateCode;

    @ApiModelProperty("模板状态:  0-草稿，1-使用中，2-停用")
    private Integer status;

    @ApiModelProperty("短信模板名称，如果是通知模板下的短信模板，则无需填写")
    private String name;

    @ApiModelProperty("短信模板内容预览，如果是通知模板下的短信模板，则无需填写")
    private String content;
}
