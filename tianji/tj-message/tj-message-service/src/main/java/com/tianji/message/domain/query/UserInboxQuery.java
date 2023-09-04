package com.tianji.message.domain.query;

import com.tianji.common.domain.query.PageQuery;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "通知模板查询对象")
@Data
public class UserInboxQuery extends PageQuery {
    private Boolean isRead;
    private Integer type;
}
