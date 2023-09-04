package com.tianji.data.model.vo;

import com.tianji.common.utils.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName AxisVO
 * @Author wusongsong
 * @Date 2022/10/10 10:55
 * @Version
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AxisVO {

    //数值轴
    public static final String TYPE_VALUE = "value";
    //类目轴
    public static final String TYPE_CATEGORY = "category";
    //时间轴
    public static final String TYPE_TIME = "time";
    //对数轴
    public static final String TYPE_LOG = "log";

    private String type;
    //最大值 带单位
    private Double max;
    //最小值 带单位
    private Double min;
    //平均值不带单位
    private Double average;
    // 数据
    private List<?> data;
    //interval
    private Double interval;


    public static AxisVO last15Day() {

        return new AxisVO(
                TYPE_CATEGORY,
                null,
                null,
                null,
                DateUtils.last15Day(),
                null
        );
    }

}
