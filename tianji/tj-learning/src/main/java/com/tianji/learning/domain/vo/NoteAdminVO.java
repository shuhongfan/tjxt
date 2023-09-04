package com.tianji.learning.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel(description = "管理端笔记分页信息")
public class NoteAdminVO {
    @ApiModelProperty("主键id")
    private Long id;
    @ApiModelProperty("课程名称")
    private String courseName;
    @ApiModelProperty("章名称")
    private String chapterName;
    @ApiModelProperty("节名称")
    private String sectionName;
    @ApiModelProperty("笔记内容")
    private String content;
    @ApiModelProperty("是否被隐藏")
    private Boolean hidden;
    @ApiModelProperty("作者名称")
    private String authorName;
    @ApiModelProperty(value = "笔记发布时间", example = "2022-7-18 19:52:36")
    private LocalDateTime createTime;
}
