package com.tianji.course.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author wusongsong
 * @since 2022/7/26 9:26
 * @version 1.0.0
 **/
@Data
public class CourseSimpleInfoListDTO {

    @ApiModelProperty("三级分类id列表")
    private List<Long> thirdCataIds;

    @ApiModelProperty("课程id列表")
    private List<Long> ids;
}
