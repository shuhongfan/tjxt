package com.tianji.course.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author wusongsong
 * @since 2022/7/10 15:06
 * @version 1.0.0
 **/
@Data
public class CategoryInfoVO {
    @ApiModelProperty("课程分类id")
    private Long id;
    @ApiModelProperty("课程分类名称")
    private String name;
    @ApiModelProperty("状态：1：正常，2：禁用")
    private Integer status;
    @ApiModelProperty("状态描述")
    private String statusDesc;
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
    @ApiModelProperty("分类级别，1：一级分类，2：二级分类，3：三级分类")
    private Integer categoryLevel;
    @ApiModelProperty("一级分类名称")
    private String firstCategoryName;
    @ApiModelProperty("二级分类名称")
    private String secondCategoryName;
    @ApiModelProperty("排序")
    private Integer index;
}
