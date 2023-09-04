package com.tianji.trade.domain.po;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 订单
 * </p>
 *
 * @author 虎哥
 * @since 2022-08-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "`order`", autoResultMap = true)
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 支付交易流水单
     */
    private Long payOrderNo;
    /**
     * 用户id
     */
    private Long userId;

    /**
     * 订单状态，1：待支付，2：已支付，3：已关闭，4：已完成，5：已报名，6：已申请退款
     */
    private Integer status;

    /**
     * 状态备注
     */
    private String message;

    /**
     * 订单总金额，单位分
     */
    private Integer totalAmount;

    /**
     * 实付金额，单位分
     */
    private Integer realAmount;

    /**
     * 优惠金额，单位分
     */
    private Integer discountAmount;

    /**
     * 支付渠道
     */
    private String payChannel;

    /**
     * 优惠券id
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Long> couponIds;

    /**
     * 创建订单时间
     */
    private LocalDateTime createTime;

    /**
     * 支付时间
     */
    private LocalDateTime payTime;

    /**
     * 订单关闭时间
     */
    private LocalDateTime closeTime;

    /**
     * 订单完成时间，支付后30天
     */
    private LocalDateTime finishTime;

    /**
     * 申请退款时间
     */
    private LocalDateTime refundTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 创建人
     */

    private Long creater;

    /**
     * 更新人
     */

    private Long updater;

    /**
     * 逻辑删除
     */
    @TableLogic
    private Integer deleted;


}
