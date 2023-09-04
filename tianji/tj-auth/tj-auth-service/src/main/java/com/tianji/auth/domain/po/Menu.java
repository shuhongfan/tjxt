package com.tianji.auth.domain.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tianji.auth.domain.dto.MenuDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 权限表，包括菜单权限和访问路径权限
 * </p>
 *
 * @author 虎哥
 * @since 2022-07-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("menu")
@NoArgsConstructor
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 父菜单id，默认0代表没有父菜单
     */
    private Long parentId;

    /**
     * 是否有子菜单，默认false
     */
    private Boolean hasChildren;

    /**
     * 菜单文本
     */
    private String label;

    /**
     * 菜单路径
     */
    private String path;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 顺序优先级，默认127
     */
    private Integer priority;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 创建者id
     */

    private Long creater;

    /**
     * 更新者id
     */

    private Long updater;

    /**
     * 部门id
     */
    private Long depId;

    /**
     * 逻辑删除，默认0
     */
    private Integer deleted;


    public Menu(MenuDTO dto) {
        this.id = dto.getId();
        this.parentId = dto.getParentId();
        this.label = dto.getLabel();
        this.path = dto.getPath();
        this.icon = dto.getIcon();
        this.priority = dto.getPriority();
    }

    public MenuDTO toDTO(){
        MenuDTO dto = new MenuDTO();
        dto.setId(id);
        dto.setPath(path);
        dto.setParentId(parentId);
        dto.setLabel(label);
        dto.setIcon(icon);
        dto.setPriority(priority);
        return dto;
    }
}
