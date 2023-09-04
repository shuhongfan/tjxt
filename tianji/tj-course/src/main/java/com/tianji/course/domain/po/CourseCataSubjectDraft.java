package com.tianji.course.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

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
@TableName("course_cata_subject_draft")
public class CourseCataSubjectDraft implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 小节题目关系id
     */
    private Long id;

    private Long courseId;

    /**
     * 小节id
     */
    private Long cataId;

    /**
     * 题目id
     */
    private Long subjectId;

    private LocalDateTime createTime;


}
