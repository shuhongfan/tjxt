package com.tianji.learning.domain.query;

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
@ApiModel(description = "互动问题管理端分页查询条件")
public class QuestionAdminPageQuery extends PageQuery {
    @ApiModelProperty(value = "课程名称搜索关键字", example = "Redis")
    private String courseName;
    @ApiModelProperty(value = "管理端问题状态：0-未查看，1-已查看", example = "1")
    private Integer status;
    @ApiModelProperty(value = "更新时间区间的开始时间", example = "2022-7-18 19:52:36")
    @DateTimeFormat(pattern = DateUtils.DEFAULT_DATE_TIME_FORMAT)
    private LocalDateTime beginTime;
    @DateTimeFormat(pattern = DateUtils.DEFAULT_DATE_TIME_FORMAT)
    @ApiModelProperty(value = "更新时间区间的结束时间", example = "2022-7-18 19:52:36")
    private LocalDateTime endTime;
}
