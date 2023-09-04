package com.tianji.pay.domain.po;

import com.baomidou.mybatisplus.annotation.*;
import com.tianji.pay.third.model.PayStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 支付订单
 * </p>
 *
 * @author 虎哥
 * @since 2022-08-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("pay_order")
public class PayOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 业务订单号
     */
    private Long bizOrderNo;

    /**
     * 支付单号
     */
    private Long payOrderNo;

    /**
     * 支付用户id
     */
    private Long bizUserId;

    /**
     * 支付渠道id
     */
    private String payChannelCode;

    /**
     * 支付金额，单位位分
     */
    private Integer amount;

    /**
     * 支付类型，1：h5,2:小程序，3：公众号，4：扫码
     */
    private Integer payType;

    /**
     * 支付状态，0：待提交，1:待支付，2：支付成功，3：支付超时或取消
     */
    private Integer status;

    /**
     * 拓展字段，用于传递不同渠道单独处理的字段
     */
    private String expandJson;

    /**
     * 业务端回调接口
     */
    private String notifyUrl;

    /**
     * 业务端回调次数
     */
    private Integer notifyTimes;

    /**
     * 回调状态，0：待回调，1：回调成功，2：回调失败
     */
    private Integer notifyStatus;

    /**
     * 第三方返回业务码
     */
    private String resultCode;

    /**
     * 第三方返回提示信息
     */
    private String resultMsg;

    /**
     * 支付成功时间
     */
    private LocalDateTime paySuccessTime;

    /**
     * 支付超时时间
     */
    private LocalDateTime payOverTime;

    /**
     * 支付二维码
     */
    private String qrCodeUrl;

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
    private Long creater;

    /**
     * 更新人
     */
    private Long updater;

    /**
     * 逻辑删除
     */
    @TableLogic
    private Boolean deleted;


    public boolean success(){
        return PayStatus.TRADE_SUCCESS.equalsValue(status);
    }

    public boolean closed(){
        return PayStatus.TRADE_CLOSED.equalsValue(status);
    }

    public boolean waitBuyerPay(){
        return PayStatus.WAIT_BUYER_PAY.equalsValue(status);
    }

    public boolean notCommit(){
        return PayStatus.NOT_COMMIT.equalsValue(status);
    }
}
