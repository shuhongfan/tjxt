package com.tianji.api.dto.course;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 课程信息
 * @ClassName CourseDTO
 * @author wusongsong
 * @since 2022/7/18 13:12
 * @version 1.0.0
 **/
@ApiModel(description = "课程信息")
@Data
public class CourseSearchDTO {
    @ApiModelProperty("课程id")
    private Long id;
    @ApiModelProperty("课程名称")
    private String name;
    @ApiModelProperty("一级课程分类id")
    private Long categoryIdLv1;
    @ApiModelProperty("二级课程分类id")
    private Long categoryIdLv2;
    @ApiModelProperty("三级课程分类id")
    private Long categoryIdLv3;
    @ApiModelProperty("课程封面")
    private String coverUrl;
    @ApiModelProperty("价格")
    private Integer price;
    @ApiModelProperty("是否免费")
    private Boolean free;
    @ApiModelProperty("发布时间")
    private LocalDateTime publishTime;
    @ApiModelProperty("章节数")
    private Integer sections;
    @ApiModelProperty("课程时长")
    private Integer duration;
    @ApiModelProperty("老师id")
    private Long teacher;
    @ApiModelProperty("课程类型，1：直播课程，2：录播课程")
    private Integer courseType;
    @ApiModelProperty(value = "课程报名人数（销量）", example = "3920")
    private Integer sold = 0;
    @ApiModelProperty(value = "课程评价得分，45代表4.5星", example = "35")
    private Integer score = 0;
}
