package com.tianji.media.domain.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tianji.media.enums.FileStatus;
import com.tianji.media.enums.Platform;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 文件表，可以是普通文件、图片等
 * </p>
 *
 * @author 虎哥
 * @since 2022-07-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("file")
public class File implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键，文件id
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 文件在云端的唯一标示，例如：aaa.jpg
     */
    @TableField("`key`")
    private String key;

    /**
     * 文件上传时的名称
     */
    private String filename;

    /**
     * 请求id
     */
    private String requestId;

    /**
     * 状态：1-待上传 2-已上传,未使用 3-已使用
     */
    private FileStatus status;
    /**
     * 状态：1-腾讯 2-阿里
     */
    private Platform platform;
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
