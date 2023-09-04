package com.tianji.data.model.vo;

import com.tianji.data.model.po.CourseInfo;
import lombok.Data;

import java.util.List;

/**
 * @ClassName Top10DataVO
 * @Author wusongsong
 * @Date 2022/10/10 19:33
 * @Version
 **/
@Data
public class Top10DataVO {
    // 热门课程
    private List<CourseInfo> hot;
    // 热销课程
    private List<CourseInfo> hotSales;
}
