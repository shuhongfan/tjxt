package com.tianji.promotion.strategy.scope;

import com.tianji.api.dto.promotion.OrderCourseDTO;
import com.tianji.promotion.constants.ScopeType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class CourseScope implements Scope {

    private final List<Long> scopeIds;

    @Override
    public boolean canUse(OrderCourseDTO course) {
        return scopeIds.contains(course.getId());
    }

    @Override
    public ScopeType getType() {
        return ScopeType.COURSE;
    }
}
