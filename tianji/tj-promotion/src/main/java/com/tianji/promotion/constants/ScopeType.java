package com.tianji.promotion.constants;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.tianji.common.enums.BaseEnum;
import com.tianji.promotion.strategy.scope.CategoryScope;
import com.tianji.promotion.strategy.scope.CourseScope;
import com.tianji.promotion.strategy.scope.NoScope;
import com.tianji.promotion.strategy.scope.Scope;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ScopeType implements BaseEnum {
    ALL(0, "全部"){
        @Override
        public Scope buildScope(List<Long> scopeIds) {
            return new NoScope();
        }
    },
    CATEGORY(1, "指定分类"){
        @Override
        public Scope buildScope(List<Long> scopeIds) {
            return new CategoryScope(scopeIds);
        }
    },
    COURSE(2, "指定课程") {
        @Override
        public Scope buildScope(List<Long> scopeIds) {
            return new CourseScope(scopeIds);
        }
    },
    ;
    private final int value;
    private final String desc;

    public static final String CATEGORY_HANDLER_NAME = "CATEGORY";
    public static final String COURSE_HANDLER_NAME = "COURSE";

    @JsonCreator
    public static ScopeType of(Integer value) {
        if (value == null) {
            return null;
        }
        for (ScopeType status : values()) {
            if (status.value == value) {
                return status;
            }
        }
        return null;
    }

    public static String desc(Integer value) {
        ScopeType status = of(value);
        return status == null ? "" : status.desc;
    }

    public abstract Scope buildScope(List<Long> scopeIds);
}
