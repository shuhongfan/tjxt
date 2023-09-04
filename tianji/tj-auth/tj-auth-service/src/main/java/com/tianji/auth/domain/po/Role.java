package com.tianji.auth.domain.po;

import com.baomidou.mybatisplus.annotation.*;
import com.tianji.api.dto.auth.RoleDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 角色表
 * </p>
 *
 * @author 虎哥
 * @since 2022-07-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("role")
@NoArgsConstructor
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 角色代号，例如：admin
     */
    private String code;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 角色类型：0-固定角色（不可选）1-自定义角色
     */
    private RoleType type;

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

    public Role(RoleDTO dto) {
        this.id = dto.getId();
        this.code = dto.getCode();
        this.name = dto.getName();
    }

    public RoleDTO toDTO(){
        RoleDTO dto = new RoleDTO();
        dto.setId(id);
        dto.setCode(code);
        dto.setName(name);
        return dto;
    }

    @Getter
    public enum RoleType{
        CONSTANT(0, "固定角色"),
        CUSTOM(1, "自定义角色"),
        ;
        @EnumValue
        int value;
        String desc;

        RoleType(int value, String desc) {
            this.value = value;
            this.desc = desc;
        }
    }
}
