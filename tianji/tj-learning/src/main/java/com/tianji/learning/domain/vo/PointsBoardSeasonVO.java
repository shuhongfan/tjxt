package com.tianji.learning.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

/**
 * <p>
 * 
 * </p>
 *
 * @author 虎哥
 * @since 2022-08-02
 */
@Data
@ApiModel(description = "积分榜赛季信息")
public class PointsBoardSeasonVO {

    @ApiModelProperty("赛季id，1，就是第1赛季")
    private Integer id;

    @ApiModelProperty("赛季名称，例如：第1赛季")
    private String name;

    @ApiModelProperty("赛季开始时间")
    private LocalDate beginTime;

    @ApiModelProperty("赛季结束时间")
    private LocalDate endTime;


}
