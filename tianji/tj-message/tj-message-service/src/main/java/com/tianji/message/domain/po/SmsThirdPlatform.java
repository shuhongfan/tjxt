package com.tianji.message.domain.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 第三方云通讯平台
 * </p>
 *
 * @author 虎哥
 * @since 2022-08-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sms_third_platform")
public class SmsThirdPlatform implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 短信平台id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 短信平台名称
     */
    private String name;

    /**
     * 短信平台代码，例如：ali
     */
    private String code;

    /**
     * 数字越小优先级越高，最小为0
     */
    private Integer priority;

    /**
     * 短信平台状态：0-禁用，1-启用
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
