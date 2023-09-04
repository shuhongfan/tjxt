package com.tianji.message.domain.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 通知模板
 * </p>
 *
 * @author 虎哥
 * @since 2022-08-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("notice_template")
public class NoticeTemplate implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 通知模板id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 通知模板名称
     */
    private String name;

    /**
     * 通知模板代号，例如verify-code
     */
    private String code;

    /**
     * 通知类型：0-系统通知，1-笔记通知，2-问答通知，3-其它通知
     */
    private Integer type;

    /**
     * 模板状态:  0-草稿，1-使用中，2-停用
     */
    private Integer status;

    /**
     * 通知标题
     */
    private String title;

    /**
     * 通知内容模板
     */
    private String content;

    /**
     * 是否包含第三方短信模板，默认false
     */
    private Boolean isSmsTemplate;

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
