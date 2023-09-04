package com.tianji.promotion.strategy.scope;

import com.tianji.api.cache.CategoryCache;
import com.tianji.common.exceptions.BizIllegalException;
import com.tianji.common.utils.CollUtils;
import com.tianji.promotion.constants.ScopeType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component(ScopeType.CATEGORY_HANDLER_NAME)
public class CategoryScopeNameHandler implements ScopeNameHandler {

    private final CategoryCache categoryCache;

    @Override
    public List<String> getNameByIds(List<Long> scopeIds) {
        List<String> names = categoryCache.getNameByLv3Ids(scopeIds);
        if (CollUtils.isEmpty(names)) {
            throw new BizIllegalException("分类信息不存在");
        }
        return names;
    }
}
