package com.tianji.course.domain.po;

import lombok.Data;

import java.util.List;

/**
 * @ClassName Category3PO
 * @Author wusongsong
 * @Date 2022/10/13 15:32
 * @Version
 **/
@Data
public class Category3PO {
    private Long firstCateId;
    private Long secondCateId;
    private Long thirdCateId;

    public void setId(List<Long> categoryIdList) {
        if(firstCateId != null) {
            categoryIdList.add(firstCateId);
        }
        if (secondCateId != null) {
            categoryIdList.add(secondCateId);
        }
        if(thirdCateId != null) {
            categoryIdList.add(thirdCateId);
        }
    }
}
