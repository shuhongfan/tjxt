package com.tianji.auth.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "菜单表单实体")
public class MenuDTO {
    @ApiModelProperty(value = "菜单id", example = "1")
    private Long id;

    @ApiModelProperty(value = "父菜单id", example = "0")
    private Long parentId;

    @ApiModelProperty(value = "菜单文本", example = "系统管理")
    private String label;

    @ApiModelProperty(value = "菜单路径", example = "/sys/index")
    private String path;

    @ApiModelProperty(value = "菜单图标", example = "el-icon-sys")
    private String icon;

    @ApiModelProperty(value = "菜单顺序", example = "1")
    private Integer priority;
}
