package com.tianji.data.model.dto;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @ClassName BoardDataSetDTO
 * @Author wusongsong
 * @Date 2022/10/10 19:14
 * @Version
 **/
@Data
public class BoardDataSetDTO {
    @NotNull(message = "版本")
    private Integer version;
    @NotNull(message = "数据类型不能为空")
    @Min(value = 1, message = "数据类型1-9")
    @Max(value = 9, message = "数据类型1-9")
    private Integer type;
    @NotNull(message = "设置数据不能为空")
    @Size(min = 15, max = 15, message = "需要设置15天的数据")
    private List<Double> data;
}
