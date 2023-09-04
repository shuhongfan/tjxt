package com.tianji.message.domain.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 第三方短信平台签名和模板信息
 * </p>
 *
 * @author 虎哥
 * @since 2022-08-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("message_template")
public class MessageTemplate implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 短信发送模板id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 模板名称
     */
    private String name;

    /**
     * 第三方短信推送渠道id
     */
    private String platformCode;

    /**
     * 签名
     */
    private String signName;

    /**
     * 第三方短信模板code
     */
    private String thirdTemplateCode;

    /**
     * 第三方短信模板内容预览
     */
    private String content;

    /**
     * 通知模板id，统用系统公告类短信会关联这个id
     */
    private Long templateId;

    /**
     * 短信模板状态，0-禁用，1-启用
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
