package com.tianji.course.domain.dto;

import com.tianji.course.constants.CourseErrorInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 保存老师课程关系
 * @ClassName CourseTeacherSaveDTO
 * @Author wusongsong
 * @Date 2022/7/13 14:59
 * @Version
 **/
@Data
@ApiModel("课程老师关系模型")
public class CourseTeacherSaveDTO {
    @ApiModelProperty("课程id")
    @NotNull(message = CourseErrorInfo.Msg.COURSE_TEACHER_SAVE_COURSE_ID_NULL)
    private Long id;
    @ApiModelProperty("老师id和用户端是否展示，该列表按照界面上的顺序")
    @NotNull(message = CourseErrorInfo.Msg.COURSE_TEACHER_SAVE_TEACHERS_NULL)
//    @Min(value = 1, message = CourseErrorInfo.Msg.COURSE_TEACHER_SAVE_TEACHERS_NULL)
    @Size(min = 1, max = 5, message = CourseErrorInfo.Msg.COURSE_TEACHER_SAVE_TEACHERS_NUM_MAX )
    private List<TeacherInfo> teachers;

    @Data
    @ApiModel("老师id和用户端是否显示")
    public static class TeacherInfo{
        @ApiModelProperty("老师id")
        @NotNull(message = CourseErrorInfo.Msg.COURSE_TEACHER_SAVE_TEACHER_ID_NULL)
        private Long id;
        @ApiModelProperty("用户端是否展示")
        @NotNull(message = CourseErrorInfo.Msg.COURSE_TEACHER_SAVE_TEACHER_SHOW)
        private Boolean isShow;
    }
}
