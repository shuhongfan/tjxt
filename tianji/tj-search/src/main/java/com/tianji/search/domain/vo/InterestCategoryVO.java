package com.tianji.search.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "感兴趣的分类")
public class InterestCategoryVO {
    @ApiModelProperty(value = "分类id", example = "1")
    private Long id;
    @ApiModelProperty(value = "分类名称", example = "Java")
    private String name;
}
