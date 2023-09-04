package com.tianji.api.dto.course;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@ApiModel("课程信息")
@Data
public class CourseDTO {
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
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
    @ApiModelProperty("价格")
    private Integer price;
    @ApiModelProperty("视频播放时长")
    private Integer duration;
    @ApiModelProperty("课程有效期天数")
    private Integer validDuration;
    @ApiModelProperty("是否免费")
    private Boolean free;
    @ApiModelProperty("发布时间")
    private LocalDateTime publishTime;
    @ApiModelProperty("章节数")
    private Integer sections;
    @ApiModelProperty("课程状态")
    private  Byte status;
    @ApiModelProperty("老师id")
    private Long teacher;
    @ApiModelProperty("课程类型，1：直播课程，2：录播课程")
    private Integer courseType;
    @ApiModelProperty("更新时间")
    private Long updater;
    @ApiModelProperty("课程进行到的步骤，1：基本信息，2：目录，3：课程视频，4：课程题目，5：课程老师")
    private Integer step;
    @ApiModelProperty(value = "课程报名人数（销量）", example = "3920")
    private Integer sold = 0;
    @ApiModelProperty(value = "课程评价得分，45代表4.5星", example = "35")
    private Integer score = 0;
    @ApiModelProperty("课程是否禁用,0:禁用，1：启用")
    private Integer enable;
}