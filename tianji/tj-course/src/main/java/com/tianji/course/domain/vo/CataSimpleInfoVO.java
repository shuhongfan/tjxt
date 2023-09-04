package com.tianji.course.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wusongsong
 * @since 2022/7/26 9:28
 * @version 1.0.0
 **/
@Data
@ApiModel(description = "目录简单信息")
@AllArgsConstructor
@NoArgsConstructor
public class CataSimpleInfoVO {
    @ApiModelProperty("目录id")
    private Long id;
    @ApiModelProperty("目录名称")
    private String name;
    @ApiModelProperty("目录序号1-1")
    private String index;
    @ApiModelProperty("数字序号，不包含章序号")
    private Integer cIndex;
    @ApiModelProperty("数字序号章序号")
    private Integer chapterIndex;
}
