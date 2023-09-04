package com.tianji.course.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(description = "课程和目录及学习进度信息")
public class CourseAndSectionVO {
    @ApiModelProperty("id")
    private Long id;
    @ApiModelProperty("课程名称")
    private String name;
    @ApiModelProperty("课程封面")
    private String coverUrl;
    @ApiModelProperty("课程章节数量")
    private Integer sections;
    @ApiModelProperty("教师头像")
    private String teacherIcon;
    @ApiModelProperty("教师名称")
    private String teacherName;
    @ApiModelProperty("id")
    private Long lessonId;
    @ApiModelProperty("正在学习的小节id")
    private Long latestSectionId;
    private List<ChapterVO> chapters;
}