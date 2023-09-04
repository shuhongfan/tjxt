package com.tianji.promotion.strategy.scope;

import java.util.List;

public interface ScopeNameHandler {
    List<String> getNameByIds(List<Long> scopeIds);
}
