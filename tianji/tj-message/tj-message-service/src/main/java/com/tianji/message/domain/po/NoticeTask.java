package com.tianji.message.domain.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 系统通告的任务表，可以延期或定期发送通告
 * </p>
 *
 * @author 虎哥
 * @since 2022-08-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("notice_task")
public class NoticeTask implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 公告任务id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 任务对应的通知模板id
     */
    private Long templateId;

    /**
     * 任务名称
     */
    private String name;

    /**
     * true-通知所有人;false-通知部分人。默认false
     */
    private Boolean partial;

    /**
     * 任务预期执行时间
     */
    private LocalDateTime pushTime;

    /**
     * 任务重复执行次数上限，0则不重复
     */
    private Integer maxTimes;

    /**
     * 任务延迟执行时间间隔，单位是分钟
     */
    private Integer interval;

    /**
     * 任务失效时间
     */
    private LocalDateTime expireTime;

    /**
     * 任务是否已经完成
     */
    private Boolean finished;

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
