package com.tianji.learning.domain.query;

import com.tianji.common.domain.query.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(description = "互动回答分页查询条件")
public class ReplyPageQuery extends PageQuery {
    @ApiModelProperty(value = "问题id，不为空则代表根据问题查询回答")
    private Long questionId;
    @ApiModelProperty(value = "回答id，不为空则代表根据回答查询评论")
    private Long answerId;
}
