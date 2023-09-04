package com.tianji.message.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 公告消息模板
 * </p>
 *
 * @author 虎哥
 * @since 2022-08-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("public_notice")
public class PublicNotice implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 公告id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 公告类型
     */
    private Integer type;
    /**
     * 公告标题
     */
    private String title;

    /**
     * 公告通知内容，可以存放公告消息模板
     */
    private String content;

    /**
     * 公告预期发送时间
     */
    private LocalDateTime pushTime;

    /**
     * 通知发布时间
     */
    private LocalDateTime createTime;

    /**
     * 通知失效时间
     */
    private LocalDateTime expireTime;


}
