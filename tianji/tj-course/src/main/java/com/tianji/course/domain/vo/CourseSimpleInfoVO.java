package com.tianji.course.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 课程简单信息
 * @author wusongsong
 * @since 2022/7/11 20:56
 * @version 1.0.0
 **/
@Data
@ApiModel("课程简单信息")
public class CourseSimpleInfoVO {
    @ApiModelProperty("课程id")
    private Long id;
    @ApiModelProperty("课程名称")
    private String name;
    @ApiModelProperty("封面url")
    private String coverUrl;
    @ApiModelProperty("价格")
    private Integer price;
    @ApiModelProperty("一级分类id")
    private Long firstCateId;
    @ApiModelProperty("二级分类id")
    private Long secondCateId;
    @ApiModelProperty("三级分类id")
    private Long thirdCateId;

    @ApiModelProperty("章节数量")
    private Integer sectionNum;
    @ApiModelProperty("课程有效期")
    private Integer validDuration;
    @ApiModelProperty("课程过期时间")
    private LocalDateTime purchaseEndTime;
}
