package com.tianji.exam.domain.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 题目
 * </p>
 *
 * @author 虎哥
 * @since 2022-09-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("question")
public class Question implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 题目id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 题干
     */
    private String name;

    /**
     * 题目类型，1：单选题，2：多选题，3：不定向选择题，4：判断题，5：主观题
     */
    private Integer type;

    /**
     * 1级课程分类id
     */
    private Long cateId1;

    /**
     * 2级课程分类id
     */
    private Long cateId2;

    /**
     * 3级课程分类id
     */
    private Long cateId3;

    /**
     * 难易度，1：简单，2：中等，3：困难
     */
    private Integer difficulty;

    /**
     * 回答正确次数
     */
    private Integer correctTimes;

    /**
     * 回答次数
     */
    private Integer answerTimes;

    /**
     * 分值
     */
    private Integer score;

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


}
