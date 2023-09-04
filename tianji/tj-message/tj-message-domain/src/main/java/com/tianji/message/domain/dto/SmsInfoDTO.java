package com.tianji.message.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

@Data
@ApiModel(description = "短信发送参数")
public class SmsInfoDTO {
    @ApiModelProperty("模板代号")
    private String templateCode;
    @ApiModelProperty("手机号码")
    private Iterable<String> phones;
    @ApiModelProperty("模板参数")
    private Map<String, String> templateParams;
}
