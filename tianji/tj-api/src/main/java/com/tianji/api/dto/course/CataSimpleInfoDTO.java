package com.tianji.api.dto.course;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wusongsong
 * @since 2022/7/27 14:22
 * @version 1.0.0
 **/
@Data
public class CataSimpleInfoDTO {
    @ApiModelProperty("目录id")
    private Long id;
    @ApiModelProperty("目录名称")
    private String name;
    @ApiModelProperty("数字序号，不包含章序号")
    private Integer cIndex;
}
