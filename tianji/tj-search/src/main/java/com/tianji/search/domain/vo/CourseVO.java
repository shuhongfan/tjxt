package com.tianji.search.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "课程信息")
public class CourseVO {
    @ApiModelProperty(value = "课程id", example = "1")
    private Long id;
    @ApiModelProperty(value = "课程名称", example = "Java")
    private String name;
    @ApiModelProperty(value = "课程价格，单位分", example = "32900")
    private Long price;
    @ApiModelProperty(value = "老师名字", example = "张老师")
    private String teacher;
    @ApiModelProperty(value = "老师头像", example = "default-user-icon.jpg")
    private String icon;
    @ApiModelProperty(value = "课程时长，单位秒", example = "3280")
    private Integer duration;
    @ApiModelProperty(value = "课程封面地址", example = "default-cover-url.jpg")
    private String coverUrl;
    @ApiModelProperty(value = "课程章节数量", example = "25")
    private Integer sections;
    @ApiModelProperty(value = "课程报名人数（销量）", example = "3920")
    private Integer sold;

    public static final String[] EXCLUDE_FIELDS =
            {"categoryIdLv1", "categoryIdLv2", "categoryIdLv3", "free",
                    "publishTime", "type", "status", "score"};
}
