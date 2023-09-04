package com.tianji.api.dto.course;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 课程信息
 *
 * @author wusongsong
 * @since 2022/8/5 16:54
 * @version 1.0.0
 **/
@Data
@ApiModel(description = "课程详细信息，包含课程、目录、教师")
public class CourseFullInfoDTO {
    @ApiModelProperty("课程id")
    private Long id;
    @ApiModelProperty("课程名称")
    private String name;
    @ApiModelProperty("封面链接")
    private String coverUrl;
    @ApiModelProperty("价格")
    private Integer price;
    @ApiModelProperty("一级课程分类id")
    private Long firstCateId;
    @ApiModelProperty("二级课程分类id")
    private Long secondCateId;
    @ApiModelProperty("三级课程分类id")
    private Long thirdCateId;
    @ApiModelProperty("课程总节数")
    private Integer sectionNum;
    @ApiModelProperty("课程购买有效期结束时间")
    private LocalDateTime purchaseEndTime;
    @ApiModelProperty("课程学习有效期")
    private Integer validDuration;
    @ApiModelProperty("课程章信息")
    private List<CatalogueDTO> chapters;
    @ApiModelProperty("老师列表")
    private List<Long> teacherIds;
    @JsonIgnore
    public List<Long> getCategoryIds(){
        return List.of(firstCateId, secondCateId, thirdCateId);
    }
}
