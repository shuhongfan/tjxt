package com.tianji.message.domain.dto;

import com.tianji.common.domain.dto.BaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(description = "通知模板实体")
public class NoticeTemplateDTO extends BaseDTO {
    @ApiModelProperty("主键id，新增时无需填写")
    private Long id;
    @ApiModelProperty("模板名称")
    private String name;
    @ApiModelProperty("模板代号，例如：VERIFY_CODE")
    private String code;
    @ApiModelProperty("通知类型：0-系统通知，1-笔记通知，2-问答通知，3-其它通知")
    private Integer type;
    @ApiModelProperty("模板状态:  0-草稿，1-使用中，2-停用")
    private Integer status;
    @ApiModelProperty("通知标题")
    private String title;
    @ApiModelProperty("通知内容")
    private String content;
    @ApiModelProperty("是否是短信模板，默认false")
    private Boolean isSmsTemplate;
}
