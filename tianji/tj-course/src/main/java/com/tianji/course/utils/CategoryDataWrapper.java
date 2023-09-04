package com.tianji.course.utils;

import com.tianji.common.utils.TreeDataUtils;
import com.tianji.course.domain.po.Category;
import com.tianji.course.domain.vo.SimpleCategoryVO;

import java.util.List;

/**
 * @ClassName CategoryDataWrapper
 * @Author wusongsong
 * @Date 2022/9/21 17:45
 * @Version
 **/
public class CategoryDataWrapper implements TreeDataUtils.DataProcessor<SimpleCategoryVO, Category> {

    @Override
    public Object getParentKey(Category category) {
        return category.getParentId();
    }

    @Override
    public Object getKey(Category category) {
        return category.getId();
    }

    @Override
    public Object getRootKey() {
        return 0L;
    }

    @Override
    public List<SimpleCategoryVO> getChild(SimpleCategoryVO simpleCategoryVO) {
        return simpleCategoryVO.getChildren();
    }

    @Override
    public void setChild(SimpleCategoryVO parent, List<SimpleCategoryVO> child) {
        parent.setChildren(child);
    }
}
