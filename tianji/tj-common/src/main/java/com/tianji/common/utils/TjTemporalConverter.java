package com.tianji.common.utils;

import cn.hutool.core.convert.impl.TemporalAccessorConverter;

import java.time.temporal.TemporalAccessor;

public class TjTemporalConverter extends TemporalAccessorConverter {
    public TjTemporalConverter(Class<?> targetType) {
        super(targetType);
    }

    public TjTemporalConverter(Class<?> targetType, String format) {
        super(targetType, format);
    }

    @Override
    protected TemporalAccessor convertInternal(Object value) {
        if (value instanceof String) {
            String val = value.toString();
            try {
                return super.convertInternal(Long.parseLong(val));
            } catch (NumberFormatException e) {
                return super.convertInternal(value);
            }
        }
        return super.convertInternal(value);
    }
}
