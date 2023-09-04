package com.tianji.media.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel(description = "视频文件信息")
public class MediaVO {
    @ApiModelProperty(value = "id", example = "1")
    private Long id;
    @ApiModelProperty(value = "文件名称", example = "文件名.avi")
    private String filename;
    @ApiModelProperty(value = "视频封面", example = "default-cover-url.jpg")
    private String coverUrl;
    @ApiModelProperty(value = "视频时长，单位秒", example = "57.23")
    private Float duration;
    @ApiModelProperty(value = "视频大小，单位字节", example = "1024")
    private Long size;
    @ApiModelProperty(value = "被引用次数", example = "10")
    private Integer useTimes;
    @ApiModelProperty(value = "视频状态：1-上传中，2-已上传，3-处理中", example = "2")
    private Integer status;
    @ApiModelProperty(value = "创建时间", example = "2022-7-18 16:54:30")
    private LocalDateTime createTime;
    @ApiModelProperty(value = "创建者名称", example = "张三")
    private String creater;
}
