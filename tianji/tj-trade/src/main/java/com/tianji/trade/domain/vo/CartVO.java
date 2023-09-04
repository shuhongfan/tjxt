package com.tianji.trade.domain.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel(description = "购物车条目信息")
public class CartVO {
    @ApiModelProperty("购物车中条目id")
    private Long id;
    @ApiModelProperty("课程id")
    private Long courseId;
    @ApiModelProperty("课程名称")
    private String courseName;
    @ApiModelProperty("课程封面url")
    private String coverUrl;
    @ApiModelProperty("加入购物车时的课程价格，单位元")
    private Integer price;
    @ApiModelProperty("现在的课程价格，单位元")
    private Integer nowPrice;
    @ApiModelProperty("课程是否已经过期")
    private Boolean expired;
    @JsonIgnore
    @ApiModelProperty(value = "课程有效期", hidden = true)
    private LocalDateTime courseValidDate;
}