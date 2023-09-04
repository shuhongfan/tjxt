package com.tianji.api.dto.sms;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Map;

@Data
@ApiModel(description = "短信发送参数")
public class SmsInfoDTO {
    private String templateCode;
    private Iterable<String> phones;
    private Map<String, String> templateParams;
}
