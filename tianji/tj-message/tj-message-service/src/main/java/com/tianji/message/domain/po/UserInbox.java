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
 * 用户通知记录
 * </p>
 *
 * @author 虎哥
 * @since 2022-08-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user_inbox")
public class UserInbox implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户通知id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 通知类型：0-系统通知，1-笔记通知，2-问答通知，3-其它通知
     */
    private Integer type;

    /**
     * 通知标题
     */
    private String title;

    /**
     * 通知或私信内容
     */
    private String content;

    /**
     * 公告是否已读
     */
    private Boolean isRead;

    /**
     * 通知的发送者id，0则代表是系统
     */
    private Long publisher;

    /**
     * 消息推送时间
     */
    private LocalDateTime pushTime;

    /**
     * 过期时间，一旦过期用户端不在展示
     */
    private LocalDateTime expireTime;


}
