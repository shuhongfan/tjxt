package com.tianji.course.domain.dto;

import com.tianji.course.constants.CourseErrorInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 保存章节信息
 *
 * @author wusongsong
 * @since 2022/7/11 18:10
 * @version 1.0.0
 **/
@Data
@ApiModel(description = "小节、练习和题目关系模型")
public class CataSubjectDTO {
        @ApiModelProperty("题目id")
        @NotNull(message = CourseErrorInfo.Msg.COURSE_SUBJECT_SAVE_SUBJECT_IDS_NULL)
        @Size(min = 1,message = CourseErrorInfo.Msg.COURSE_SUBJECT_SAVE_SUBJECT_IDS_NULL)
        private List<Long> subjectIds;
        @ApiModelProperty("小节或练习id")
        @NotNull(message = CourseErrorInfo.Msg.COURSE_SUBJECT_SAVE_CATALOGUE_ID_NULL)
        private Long cataId;
}
