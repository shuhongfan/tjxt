package com.tianji.trade.domain.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 退款申请
 * </p>
 *
 * @author 虎哥
 * @since 2022-08-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("refund_apply")
public class RefundApply implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 退款id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 订单明细id
     */
    private Long orderDetailId;

    /**
     * 订单id
     */
    private Long orderId;
    /**
     * 退款单号，每次退款的唯一标示
     */
    private Long refundOrderNo;

    /**
     * 订单所属用户id
     */
    private Long userId;

    /**
     * 退款金额
     */
    private Integer refundAmount;

    /**
     * 退款状态，1：待审批，2：取消退款，3：同意退款，4：拒绝退款，5：退款成功，6：退款失败
     */
    private Integer status;
    /**
     * 退款状态描述
     */
    private String message;
    /**
     * 申请退款原因
     */
    private String refundReason;
    /**
     * 退款原因描述
     */
    private String questionDesc;

    /**
     * 审批人id
     */
    private Long approver;
    /**
     * 审批意见
     */
    private String approveOpinion;
    /**
     * 审批备注
     */
    private String remark;

    /**
     * 退款渠道
     */
    private String refundChannel;
    /**
     * 退款失败原因
     */
    private String failedReason;

    /**
     * 创建退款申请时间
     */
    private LocalDateTime createTime;

    /**
     * 审批时间
     */
    private LocalDateTime approveTime;

    /**
     * 退款完成时间（成功或失败）
     */
    private LocalDateTime finishTime;

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

}
