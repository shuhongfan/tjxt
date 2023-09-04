package com.tianji.learning.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(description = "签到结果")
public class SignRecordVO {
    @ApiModelProperty("连续签到天数")
    private Integer signDays;
    @ApiModelProperty("签到记录，例如：[0,1,1,0,1]，按照数组角标0~30，代表当月1~31号, 0代表未签到，1代表已签到")
    private List<Byte> signRecords;
}
