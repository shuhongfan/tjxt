package com.tianji.pay.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tianji.pay.third.model.RefundStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 退款订单
 * </p>
 *
 * @author 虎哥
 * @since 2022-08-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("refund_order")
public class RefundOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 业务端已支付的订单id
     */
    private Long bizOrderNo;

    /**
     * 业务端要退款的订单id
     */
    private Long bizRefundOrderNo;

    /**
     * 支付单号，提交给第三方的那个
     */
    private Long payOrderNo;

    /**
     * 退款单号，每次退款的唯一标示
     */
    private Long refundOrderNo;

    /**
     * 本次退款金额，单位分
     */
    private Integer refundAmount;

    /**
     * 总金额，单位分
     */
    private Integer totalAmount;

    /**
     * 是否是部分退款
     */
    private Boolean isSplit;

    /**
     * 支付渠道id
     */
    private String payChannelCode;
    /**
     * 第三方交易编码
     */
    private String resultCode;

    /**
     * 第三方交易信息
     */
    private String resultMsg;

    /**
     * 退款状态，1：退款中，2：退款成功，3：退款失败
     */
    private Integer status;

    /**
     * 退款渠道
     */
    private String refundChannel;

    /**
     * 业务端退款通知失败次数
     */
    private Integer notifyFailedTimes;

    /**
     * 退款接口通知状态，0：待通知，1：通知成功，2：通知中，3：通知失败
     */
    private Integer notifyStatus;

    /**
     * 退款单据创建时间
     */
    private LocalDateTime createTime;

    /**
     * 退款单据修改时间
     */
    private LocalDateTime updateTime;

    /**
     * 单据创建人，一般手动对账产生的单据才有值
     */
    private Long creater;

    /**
     * 单据修改人，一般手动对账产生的单据才有值
     */
    private Long updater;

    /**
     * 逻辑删除
     */
    @TableLogic
    private Boolean deleted;


    public boolean success(){
        return RefundStatus.SUCCESS.equalsValue(status);
    }

    public boolean failed(){
        return RefundStatus.FAILED.equalsValue(status);
    }

    public boolean unknown(){
        return RefundStatus.UN_KNOWN.equalsValue(status);
    }

    public boolean notCommit(){
        return RefundStatus.NOT_COMMIT.equalsValue(status);
    }
}
