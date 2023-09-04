package com.tianji.data.model.po;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName CourseInfo
 * @Author wusongsong
 * @Date 2022/10/10 19:33
 * @Version
 **/
@Data
public class CourseInfo implements Serializable {
    private String category;
    private String name;
    private Integer newStuNum;
    private Double orderAmount;
}
