package com.tianji.learning.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel(description = "笔记信息")
public class NoteVO {
    @ApiModelProperty("主键id")
    private Long id;
    @ApiModelProperty("笔记内容")
    private String content;
    @ApiModelProperty("记录笔记时的视频播放时间，单位秒")
    private Integer noteMoment;
    @ApiModelProperty("是否是隐私笔记，默认false")
    private Boolean isPrivate;
    @ApiModelProperty("是否是采集的笔记")
    private Boolean isGathered;
    @ApiModelProperty("作者id")
    private Long authorId;
    @ApiModelProperty("作者名字")
    private String authorName;
    @ApiModelProperty("作者头像")
    private String authorIcon;
    @ApiModelProperty(value = "笔记发布时间", example = "2022-7-18 19:52:36")
    private LocalDateTime createTime;
}