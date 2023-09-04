package com.tianji.course.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 课程分类
 * </p>
 *
 * @author wusongsong
 * @since 2022-07-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("category")
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 课程分类id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 父分类id，一级分类父id为0
     */
    private Long parentId;

    /**
     * 分类级别，1,2,3：代表一级分类，二级分类，三级分类
     */
    private Integer level;

    /**
     * 同级目录优先级，数字越小优先级越高，可以重复
     */
    private Integer priority;

    /**
     * 课程分类状态，1：正常，0：禁用
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


    /**
     * 创建者
     */
    private Long creater;

    /**
     * 更新者
     */
    private Long updater;

    @TableLogic
    private Integer deleted;


}
