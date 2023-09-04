package com.tianji.message.domain.query;

import com.tianji.common.domain.query.PageQuery;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "通知模板查询对象")
@Data
public class NoticeTaskPageQuery extends PageQuery {
    private Boolean finished;
    private String keyword;
    private LocalDateTime minPushTime;
    private LocalDateTime maxPushTime;
}
