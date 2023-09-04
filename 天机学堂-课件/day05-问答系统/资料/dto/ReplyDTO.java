package com.tianji.learning.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(description = "互动回答信息")
public class ReplyDTO {
    @ApiModelProperty("回答内容")
    @NotNull(message = "回答内容不能为空")
    private String content;

    @ApiModelProperty("是否匿名提问")
    private Boolean anonymity;

    @ApiModelProperty("互动问题id")
    @NotNull(message = "问题id不能为空")
    private Long questionId;

    @ApiModelProperty("回复的上级回答id，没有可不填")
    private Long answerId;

    @ApiModelProperty("回复的目标回复id，没有可不填")
    private Long targetReplyId;

    @ApiModelProperty("回复的目标用户id，没有可不填")
    private Long targetUserId;

    @ApiModelProperty("标记是否是学生提交的回答，默认true")
    private Boolean isStudent = true;
}
