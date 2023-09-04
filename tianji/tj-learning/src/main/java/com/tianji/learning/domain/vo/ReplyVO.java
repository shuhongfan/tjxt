package com.tianji.learning.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel(description = "互动回答信息")
public class ReplyVO {
    @ApiModelProperty("id")
    private Long id;
    @ApiModelProperty("回答内容")
    private String content;
    @ApiModelProperty("是否匿名提问")
    private Boolean anonymity;
    @ApiModelProperty("是否隐藏")
    private Boolean hidden;
    @ApiModelProperty("评论数量")
    private Integer replyTimes;
    @ApiModelProperty(value = "创建时间，也就是回答时间")
    private LocalDateTime createTime;

    @ApiModelProperty("当前回复者id")
    private Long userId;
    @ApiModelProperty("当前回复者昵称")
    private String userName;
    @ApiModelProperty("当前回复者头像")
    private String userIcon;
    @ApiModelProperty("当前回复者类型，2-学员，其它-老师")
    private Integer userType;

    @ApiModelProperty("是否点过赞")
    private Boolean liked;
    @ApiModelProperty("点赞数量")
    private Integer likedTimes;
    @ApiModelProperty("目标用户名字")
    private String targetUserName;
}
