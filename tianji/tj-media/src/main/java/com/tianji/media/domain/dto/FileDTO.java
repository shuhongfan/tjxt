package com.tianji.media.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@ApiModel(description = "文件信息实体")
@NoArgsConstructor
@AllArgsConstructor
public class FileDTO {
    @ApiModelProperty(value = "文件id", example = "1")
    private Long id;
    @ApiModelProperty(value = "文件名称", example = "图片.jpg")
    private String filename;
    @ApiModelProperty(value = "文件访问路径", example = "a.jpg")
    private String path;

    public static FileDTO of(Long id, String filename, String path){
        return new FileDTO(id, filename, path);
    }
}
