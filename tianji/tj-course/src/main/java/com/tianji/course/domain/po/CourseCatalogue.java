package com.tianji.course.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 目录草稿
 * </p>
 *
 * @author wusongsong
 * @since 2022-07-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("course_catalogue")
public class CourseCatalogue implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 课程目录id
     */
    private Long id;

    /**
     * 目录名称
     */
    private String name;

    /**
     * 是否支持试看
     */
    private Integer trailer;

    /**
     * 课程id
     */
    private Long courseId;

    /**
     * 目录类型1：章，2：节，3：测试
     */
    private Integer type;

    /**
     * 所属章id，只有小节和测试有该值，章没有，章默认为0
     */
    private Long parentCatalogueId;

    /**
     * 媒资id
     */
    private Long mediaId;

    /**
     * 视频id
     */
    private Long videoId;

    /**
     * 视频名称
     */
    private String videoName;

    /**
     * 直播开始时间
     */
    private LocalDateTime livingStartTime;

    /**
     * 直播结束时间
     */
    private LocalDateTime livingEndTime;

    /**
     * 是否支持回放
     */
    private Integer playBack;

    /**
     * 视频时长，以秒为单位
     */
    private Integer mediaDuration;

    /**
     * 用于章节排序
     */
    private Integer cIndex;

    /**
     * 部门id
     */
    private Long depId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    private Long creater;

    /**
     * 更新人
     */
    private Long updater;

    /**
     * 逻辑删除
     */
    private Integer deleted;


}
