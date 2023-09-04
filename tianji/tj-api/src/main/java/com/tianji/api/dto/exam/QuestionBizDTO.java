package com.tianji.api.dto.exam;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@ApiModel(description = "题目与业务关联信息")
@Accessors(chain = true)
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class QuestionBizDTO{

    @ApiModelProperty("业务id，要关联问题的某业务id，例如小节id")
    private Long bizId;

    @ApiModelProperty("题目id")
    private Long questionId;

}
