package com.tianji.course.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 课程-题目关系表草稿
 * </p>
 *
 * @author wusongsong
 * @since 2022-07-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("course_cata_subject")
public class CourseCataSubject implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 小节题目关系id
     */
    private Long id;

    /**
     * 课程id
     */
    private Long courseId;

    /**
     * 小节id
     */
    private Long cataId;

    /**
     * 题目id
     */
    private Long subjectId;


}
