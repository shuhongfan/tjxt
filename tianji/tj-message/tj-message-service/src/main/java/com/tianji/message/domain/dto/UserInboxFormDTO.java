package com.tianji.message.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 用户通知记录
 * </p>
 *
 * @author 虎哥
 * @since 2022-08-20
 */
@Data
@ApiModel(description = "用户私信表单实体")
public class UserInboxFormDTO {

    @ApiModelProperty("目标用户id")
    private Long userId;

    @ApiModelProperty("私信内容")
    private String content;
}
