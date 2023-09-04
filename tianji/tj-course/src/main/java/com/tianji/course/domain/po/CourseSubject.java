package com.tianji.course.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 课程题目关系列表
 * </p>
 *
 * @author wusongsong
 * @since 2022-07-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("course_subject")
@NotNull
@AllArgsConstructor
public class CourseSubject implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 课程题目关系id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long courseId;

    private Long subjectId;


}
