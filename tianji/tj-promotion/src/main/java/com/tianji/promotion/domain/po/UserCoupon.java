package com.tianji.promotion.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tianji.promotion.enums.UserCouponStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户领取优惠券的记录，是真正使用的优惠券信息
 * </p>
 *
 * @author 虎哥
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user_coupon")
public class UserCoupon implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户券id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 优惠券的拥有者
     */
    private Long userId;

    /**
     * 优惠券模板id
     */
    private Long couponId;

    /**
     * 优惠券有效期开始时间
     */
    private LocalDateTime termBeginTime;

    /**
     * 优惠券有效期结束时间
     */
    private LocalDateTime termEndTime;

    /**
     * 优惠券使用时间（核销时间）
     */
    private LocalDateTime usedTime;

    /**
     * 优惠券状态，1：未使用，2：已使用，3：已失效
     */
    private UserCouponStatus status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
