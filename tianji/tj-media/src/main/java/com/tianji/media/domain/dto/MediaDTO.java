package com.tianji.media.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "视频文件信息")
public class MediaDTO {
    @ApiModelProperty(value = "数据库mediaId", example = "1")
    private Long id;

    @ApiModelProperty(value = "文件名称", example = "Redis实战课.mp4")
    private String filename;

    @ApiModelProperty(value = "视频时长，单位秒", example = "57.23")
    private Float duration;

    @ApiModelProperty(value = "视频大小，单位字节", example = "1024")
    private Long size;
}
