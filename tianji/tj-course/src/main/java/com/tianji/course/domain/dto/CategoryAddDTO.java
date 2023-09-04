package com.tianji.course.domain.dto;

import com.tianji.course.constants.CourseErrorInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 课程分类新增模型
 *
 * @author wusongsong
 * @since 2022/7/10 12:10
 * @version 1.0.0
 **/
@Data
@ApiModel(description = "课程分类新增模型")
public class CategoryAddDTO {
    @ApiModelProperty("父分类id, 如果是新增一级分类parentId传0")
    private Long parentId;

    @ApiModelProperty(value = "名称",required = true)
    @NotNull(message = CourseErrorInfo.Msg.CATEGORY_ADD_NAME_NOT_NULL)
    @Size(max = 15, message = CourseErrorInfo.Msg.CATEGORY_ADD_NAME_SIZE)
    private String name;

    @ApiModelProperty(value = "分类序号",required = true)
    @Max(value = 99, message = CourseErrorInfo.Msg.CATEGORY_ADD_INDEX_MAX_MIN)
    @Min(value = 1, message = CourseErrorInfo.Msg.CATEGORY_ADD_INDEX_MAX_MIN)
    @NotNull(message = CourseErrorInfo.Msg.CATEGORY_ADD_INDEX_NOT_NULL)
    private Integer index;

}
