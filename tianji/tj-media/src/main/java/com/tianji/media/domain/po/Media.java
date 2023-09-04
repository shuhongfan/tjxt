package com.tianji.media.domain.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tianji.media.enums.FileStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 媒资表，主要是视频文件
 * </p>
 *
 * @author 虎哥
 * @since 2022-07-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("media")
public class Media implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 文件在云端的唯一标示，例如：387702302659783576
     */
    private String fileId;

    /**
     * 文件名称
     */
    private String filename;

    /**
     * 媒体播放地址
     */
    private String mediaUrl;

    /**
     * 媒体封面地址
     */
    private String coverUrl;

    /**
     * 视频时长，单位秒
     */
    private Float duration;

    /**
     * 请求id
     */
    private String requestId;

    /**
     * 状态：1-上传中，2-已上传
     */
    private FileStatus status;
    /**
     * 媒资大小，单位字节
     */
    private Long size;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 创建者
     */

    private Long creater;

    /**
     * 更新者
     */

    private Long updater;

    /**
     * 逻辑删除
     */
    private Integer deleted;
}
