package com.tianji.api.dto.course;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "分类id和名称信息")
public class CategoryBasicDTO {
    @ApiModelProperty(value = "分类id", example = "1")
    private Long id;
    @ApiModelProperty(value = "分类名称", example = "Java")
    private String name;
    @ApiModelProperty(value = "父分类id", example = "0")
    private Long parentId;
}
