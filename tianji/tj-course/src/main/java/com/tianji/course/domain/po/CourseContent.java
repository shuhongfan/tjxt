package com.tianji.course.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 课程内容，主要是一些大文本
 * </p>
 *
 * @author wusongsong
 * @since 2022-07-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("course_content")
public class CourseContent implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 课程内容id
     */
    private Long id;

    /**
     * 课程介绍
     */
    private String courseIntroduce;

    /**
     * 适用人群
     */
    private String usePeople;

    /**
     * 课程详情
     */
    private String courseDetail;

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
