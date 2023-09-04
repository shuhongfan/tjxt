package com.tianji.course.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 三级分类
 * @ClassName CateSimpleInfoVO
 * @Author wusongsong
 * @Date 2022/7/11 20:59
 * @Version
 **/
@Data
@ApiModel("分类")
public class CateSimpleInfoDTO {
    @ApiModelProperty("一级分类")
    private Long firstCateId;
    @ApiModelProperty("二级分类id")
    private Long secondCateId;
    @ApiModelProperty("三级分类id")
    private Long thirdCateId;

}
