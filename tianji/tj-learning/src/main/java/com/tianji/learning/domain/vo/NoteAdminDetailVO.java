package com.tianji.learning.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiModel(description = "管理端笔记详情信息")
public class NoteAdminDetailVO {
    @ApiModelProperty("主键id")
    private Long id;
    @ApiModelProperty("课程名称")
    private String courseName;
    @ApiModelProperty("章名称")
    private String chapterName;
    @ApiModelProperty("节名称")
    private String sectionName;
    @ApiModelProperty("课程分类名称，以/拼接")
    private String categoryNames;
    @ApiModelProperty("笔记内容")
    private String content;
    @ApiModelProperty("记录笔记时的视频播放时间，单位秒")
    private Integer noteMoment;
    @ApiModelProperty("被采集次数")
    private Integer usedTimes;
    @ApiModelProperty("用户端是否被隐藏")
    private Boolean hidden;
    @ApiModelProperty("作者名称")
    private String authorName;
    @ApiModelProperty("作者手机号")
    private String authorPhone;
    @ApiModelProperty(value = "笔记发布时间", example = "2022-7-18 19:52:36")
    private LocalDateTime createTime;
    @ApiModelProperty("采集过笔记的用户名集合")
    private List<String> gathers;
}
