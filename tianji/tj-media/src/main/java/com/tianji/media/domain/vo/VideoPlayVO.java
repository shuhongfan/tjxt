package com.tianji.media.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "视频播放的签名信息")
public class VideoPlayVO {
    @ApiModelProperty(value = "视频唯一标示", example = "12412534535143242")
    private String fileId;
    @ApiModelProperty(value = "视频封面", example = "xxx.xxx.xxx")
    private String signature;
}
