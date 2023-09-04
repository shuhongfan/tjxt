package com.tianji.pay.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 支付渠道
 * </p>
 *
 * @author 虎哥
 * @since 2022-08-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("pay_channel")
public class PayChannel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 支付渠道id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 50
     */
    private String name;

    /**
     * 支付渠道编码，用于获取支付实现
     */
    private String channelCode;

    /**
     * 渠道优先级，数字越小优先级越高
     */
    private Integer channelPriority;

    /**
     * 渠道图标
     */
    private String channelIcon;

    /**
     * 支付渠道状态，1：使用中，2：停用
     */
    private Integer status;

    /**
     * 创建人
     */
    private Long creater;

    /**
     * 更新人
     */
    private Long updater;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
