package com.tianji.common.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@ApiModel(description = "id和name键值对")
@NoArgsConstructor
@AllArgsConstructor
public class IdNameDTO {
    @ApiModelProperty("id")
    private Long id;
    @ApiModelProperty("name")
    private String name;
}
