package com.tianji.exam.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

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
@TableName(value = "question_detail", autoResultMap = true)
public class QuestionDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 题目id
     */
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 选择题选项，json数组格式
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> options;

    /**
     * 选择题正确答案1到10，如果有多个答案，中间使用逗号隔开，如果是判断题，1：代表正确，其他代表错误
     */
    private String answer;

    /**
     * 答案解析
     */
    private String analysis;
}
