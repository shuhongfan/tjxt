package com.tianji.course.domain.dto;

import com.tianji.common.domain.query.PageQuery;
import com.tianji.common.utils.DateUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(description = "课程搜索条件")
public class CoursePageQuery extends PageQuery {

    @ApiModelProperty(value = "搜索关键字", example = "Redis")
    private String keyword;
    @ApiModelProperty(value = "课程1级分类id", example = "1")
    private Long firstCateId;
    @ApiModelProperty(value = "课程2级分类id", example = "2")
    private Long secondCateId;
    @ApiModelProperty(value = "课程3级分类id", example = "3")
    private Long thirdCateId;
    @ApiModelProperty(value = "售卖模式，true：免费，false：收费", example = "true")
    private Boolean free;
    @ApiModelProperty(value = "课程状态，1：待上架，2：已上架，3：已下架，4：已完结", example = "1", required = true)
    private Integer status;
    @ApiModelProperty(value = "课程类型，1-录播，2-直播", example = "1")
    private Integer courseType;
    @ApiModelProperty(value = "更新时间区间的开始时间", example = "2022-7-18 19:52:36")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime beginTime;
    @DateTimeFormat(pattern = DateUtils.DEFAULT_DATE_TIME_FORMAT)
    @ApiModelProperty(value = "更新时间区间的结束时间", example = "2022-7-18 19:52:36")
    private LocalDateTime endTime;
}
