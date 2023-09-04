package com.tianji.course.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 课程相关老师信息
 * @ClassName CourseTeacherVO
 * @Author wusongsong
 * @Date 2022/7/11 18:17
 * @Version
 **/
@Data
@ApiModel("老师课程信息")
public class CourseTeacherVO {
    @ApiModelProperty("老师课程关系id")
    private Long id;
    @ApiModelProperty("老师头像")
    private String icon;
    @ApiModelProperty("形象照")
    private String photo;
    @ApiModelProperty("老师姓名")
    private String name;
    @ApiModelProperty("老师介绍")
    private String introduce;
    @ApiModelProperty("用户端是否显示")
    private Boolean isShow;
    @ApiModelProperty("职位")
    private String job;

}
