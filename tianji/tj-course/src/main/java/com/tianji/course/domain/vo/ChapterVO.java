package com.tianji.course.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(description = "章信息")
public class ChapterVO {
    @ApiModelProperty("章id")
    private Long id;
    @ApiModelProperty("章索引")
    private Integer index;
    @ApiModelProperty("章名称")
    private String name;
    @ApiModelProperty("本章视频总时长")
    private Integer mediaDuration;

    private List<SectionVO> sections;
}
