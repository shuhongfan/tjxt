package com.tianji.message.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>
 * 用户通知记录
 * </p>
 *
 * @author 虎哥
 * @since 2022-08-20
 */
@Data
@ApiModel(description = "用户收件箱消息")
public class UserInboxDTO{

    @ApiModelProperty("收件箱消息id")
    private Long id;

    @ApiModelProperty("通知类型：0-系统通知，1-笔记通知，2-问答通知，3-其它通知，4-私信")
    private Integer type;

    @ApiModelProperty("通知标题")
    private String title;

    @ApiModelProperty("通知或私信内容")
    private String content;

    @ApiModelProperty("是否已读")
    private Boolean isRead;

    @ApiModelProperty("消息发送者id")
    private Long publisher;

    @ApiModelProperty("收件箱消息id")
    private LocalDateTime pushTime;
}
