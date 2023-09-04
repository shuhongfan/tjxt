package com.tianji.learning.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(description = "笔记信息")
public class NoteFormDTO {
    @ApiModelProperty("笔记内容")
    private Long id;
    @NotNull(message = "笔记内容不能为空")
    @ApiModelProperty("笔记内容")
    private String content;
    @NotNull(message = "笔记时间不能为空")
    @ApiModelProperty("记录笔记时的视频播放时间，单位秒")
    private Integer noteMoment;
    @NotNull(message = "隐私标记不能为空")
    @ApiModelProperty("是否是隐私笔记，默认false")
    private Boolean isPrivate;
    @NotNull(message = "课程id不能为空")
    @ApiModelProperty("课程id")
    private Long courseId;
    @NotNull(message = "章id不能为空")
    @ApiModelProperty("章id")
    private Long chapterId;
    @NotNull(message = "节id不能为空")
    @ApiModelProperty("小节id")
    private Long sectionId;
}