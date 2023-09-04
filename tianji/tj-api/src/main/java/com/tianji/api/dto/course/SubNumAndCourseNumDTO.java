package com.tianji.api.dto.course;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 老师负责的课程数和出题数目的集合
 * @ClassName SubNumAndCourseNumDTO
 * @author wusongsong
 * @since 2022/7/18 15:12
 * @version 1.0.0
 **/
@Data
@AllArgsConstructor
@NotNull
@ApiModel("老师id和老师对应的课程数，出题数")
public class SubNumAndCourseNumDTO {
    @ApiModelProperty("老师id")
    private Long teacherId;
    @ApiModelProperty("老师负责的课程数")
    private Integer courseNum;
    @ApiModelProperty("老师出题数")
    private Integer subjectNum;
}
