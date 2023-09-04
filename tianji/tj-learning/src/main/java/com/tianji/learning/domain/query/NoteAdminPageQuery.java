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
@ApiModel(description = "学习笔记分页查询条件")
public class NoteAdminPageQuery extends PageQuery {
    @ApiModelProperty(value = "搜索关键字", example = "Redis")
    private String name;
    @ApiModelProperty(value = "笔记是否隐藏：true-隐藏，false-显示", example = "true")
    private Boolean hidden;
    @ApiModelProperty(value = "更新时间区间的开始时间", example = "2022-7-18 19:52:36")
    @DateTimeFormat(pattern = DateUtils.DEFAULT_DATE_TIME_FORMAT)
    private LocalDateTime beginTime;
    @ApiModelProperty(value = "更新时间区间的结束时间", example = "2022-7-18 19:52:36")
    @DateTimeFormat(pattern = DateUtils.DEFAULT_DATE_TIME_FORMAT)
    private LocalDateTime endTime;
}
