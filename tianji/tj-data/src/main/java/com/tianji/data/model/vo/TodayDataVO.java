package com.tianji.data.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName TodayDataVO
 * @Author wusongsong
 * @Date 2022/10/13 9:23
 * @Version
 **/
@Data
public class TodayDataVO {
    @ApiModelProperty("访问量，万次单位")
    private Double visits;
    @ApiModelProperty("今日订单金额,万元单位")
    private Double orderAmount;
    @ApiModelProperty("今日订单笔数")
    private Integer orderNum;
    @ApiModelProperty("今日新增学员数")
    private Integer stuNewNum;
}
