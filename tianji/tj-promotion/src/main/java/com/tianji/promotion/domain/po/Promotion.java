package com.tianji.promotion.domain.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 促销活动，形式多种多样，例如：优惠券
 * </p>
 *
 * @author 虎哥
 * @since 2022-09-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("promotion")
public class Promotion implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 促销活动id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 活动名称
     */
    private String name;

    /**
     * 促销活动类型：1-优惠券，2-分销
     */
    private Integer type;

    /**
     * 是否是热门活动：true或false，默认false
     */
    private Integer hot;

    /**
     * 活动开始时间
     */
    private LocalDateTime beginTime;

    /**
     * 活动结束时间
     */
    private LocalDateTime endTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private Long creater;

    /**
     * 更新人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updater;


}
