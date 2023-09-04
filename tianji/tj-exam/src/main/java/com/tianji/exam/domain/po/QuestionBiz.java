package com.tianji.exam.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 问题和业务关联表，例如把小节id和问题id关联，一个小节下可以有多个问题
 * </p>
 *
 * @author 虎哥
 * @since 2022-09-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("question_biz")
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class QuestionBiz implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 业务id，要关联问题的某业务id，例如小节id
     */
    private Long bizId;

    /**
     * 问题id
     */
    private Long questionId;


}
