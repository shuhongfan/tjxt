package com.tianji.auth.domain.vo;

import com.tianji.auth.domain.po.Menu;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(description = "菜单选项实体")
public class MenuOptionVO {
    @ApiModelProperty(value = "菜单id", example = "1")
    private Long id;

    @ApiModelProperty(value = "父菜单id", example = "0")
    private Long parentId;

    @ApiModelProperty(value = "菜单文本", example = "系统管理")
    private String label;

    @ApiModelProperty(value = "菜单图标", example = "el-icon-sys")
    private String icon;

    @ApiModelProperty(value = "是否有子菜单", example = "false")
    private Boolean hasChildren;

    @ApiModelProperty(value = "菜单顺序", example = "1")
    private Integer priority;

    @ApiModelProperty(value = "子菜单集合")
    private List<MenuOptionVO> subMenus;

    public MenuOptionVO() {
    }

    public MenuOptionVO(Menu menu) {
        this.id = menu.getId();
        this.parentId = menu.getParentId();
        this.label = menu.getLabel();
        this.icon = menu.getIcon();
        this.hasChildren = menu.getHasChildren();
        this.priority = menu.getPriority();
    }
}
