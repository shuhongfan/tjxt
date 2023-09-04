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
 * 课程分类更新类，只更新名称和排序
 * @author wusongsong
 * @since 2022/7/10 15:32
 * @version 1.0.0
 **/
@Data
@ApiModel(description = "分类信息更新模型")
public class CategoryUpdateDTO {
    @ApiModelProperty("分类id")
    @NotNull(message = CourseErrorInfo.Msg.CATEGORY_ID_NOT_NULL)
    private Long id;
    @ApiModelProperty("名称")
    @NotNull(message = CourseErrorInfo.Msg.CATEGORY_UPDATE_NAME_NOT_NULL)
    @Size(max = 15, message = CourseErrorInfo.Msg.CATEGORY_UPDATE_NAME_SIZE)
    private String name;
    @ApiModelProperty("分类序号")
    @Max(value = 99, message = CourseErrorInfo.Msg.CATEGORY_UPDATE_INDEX_MAX_MIN)
    @Min(value = 1, message = CourseErrorInfo.Msg.CATEGORY_UPDATE_INDEX_MAX_MIN)
    @NotNull(message = CourseErrorInfo.Msg.CATEGORY_UPDATE_INDEX_NOT_NULL)
    private Integer index;
}
