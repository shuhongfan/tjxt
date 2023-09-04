package com.tianji.api.dto.course;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author wusongsong
 * @since 2022/7/27 14:32
 * @version 1.0.0
 **/
@Data
public class CourseSimpleInfoDTO {
    @ApiModelProperty("课程id")
    private Long id;
    @ApiModelProperty("课程名称")
    private String name;
    @ApiModelProperty("封面url")
    private String coverUrl;
    @ApiModelProperty("价格")
    private Integer price;
    @ApiModelProperty("课程状态")
    private Integer status;
    @ApiModelProperty("是否是免费课程")
    private Boolean free;
    @ApiModelProperty("一级分类id")
    private Long firstCateId;
    @ApiModelProperty("二级分类id")
    private Long secondCateId;
    @ApiModelProperty("三级分类id")
    private Long thirdCateId;
    @ApiModelProperty("小节数量")
    private Integer sectionNum;
    @ApiModelProperty("课程购买有效期结束时间")
    private LocalDateTime purchaseEndTime;
    @ApiModelProperty("课程学习有效期，单位：月")
    private Integer validDuration;
    @JsonIgnore
    public List<Long> getCategoryIds(){
        return List.of(firstCateId, secondCateId, thirdCateId);
    }
}
