package com.tianji.data.model.po;

import lombok.Data;

/**
 * @ClassName TodayDataVO
 * @Author wusongsong
 * @Date 2022/10/13 9:23
 * @Version
 **/
@Data
public class TodayDataInfo {
    //访问量，万次单位
    private Double visits;
    //今日订单金额,万元单位
    private Double orderAmount;
    //今日订单笔数
    private Integer orderNum;
    //今日新增学员数
    private Integer stuNewNum;
}
