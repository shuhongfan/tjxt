package com.tianji.course.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 草稿课程
 * </p>
 *
 * @author wusongsong
 * @since 2022-07-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("course")
public class Course implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 课程草稿id，对应正式草稿id
     */
    private Long id;

    /**
     * 课程名称
     */
    private String name;

    /**
     * 课程类型，1：直播课，2：录播课
     */
    private Integer courseType;

    /**
     * 封面链接
     */
    private String coverUrl;

    /**
     * 一级课程分类id
     */
    private Long firstCateId;

    /**
     * 二级课程分类id
     */
    private Long secondCateId;

    /**
     * 三级课程分类id
     */
    private Long thirdCateId;

    /**
     * 售卖方式0付费，1：免费
     */
    private Integer free;

    /**
     * 课程价格，单位为分
     */
    private Integer price;

    /**
     * 模板类型，1：固定模板，2：自定义模板
     */
    private Integer templateType;

    /**
     * 自定义模板的连接
     */
    private String templateUrl;

    /**
     * 课程状态，1：待上架，2：已上架，3：下架，4：已完结
     */
    private Integer status;

    /**
     * 课程购买有效期开始时间
     */
    private LocalDateTime purchaseStartTime;

    /**
     * 课程购买有效期结束时间
     */
    private LocalDateTime purchaseEndTime;

    /**
     * 信息填写进度
     */
    private Integer step;

    /**
     * 课程评价得分，45代表4.5星
     */
    private Integer score;

    /**
     * 课程总时长
     */
    private Integer mediaDuration;

    /**
     * 课程有效期，单位月
     */
    private Integer validDuration;

    /**
     * 课程总节数，包括练习
     */
    private Integer sectionNum;

    /**
     * 部门id
     */
    private Long depId;

    /**
     * 发布次数
     */
    private Integer publishTimes;

    /**
     * 最近一次发布时间
     */
    private LocalDateTime publishTime;

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
