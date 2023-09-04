package com.tianji.data.model.vo;

import lombok.Data;

import java.util.List;

/**
 * echartVO
 * @ClassName EchartsVO
 * @Author wusongsong
 * @Date 2022/10/10 10:52
 * @Version
 **/
@Data
public class EchartsVO {
    private List<AxisVO> xAxis;
    private List<AxisVO> yAxis;
    private List<SerierVO> series;
}
