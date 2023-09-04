package com.tianji.common.validate;

import com.tianji.common.enums.BaseEnum;
import com.tianji.common.utils.ArrayUtils;
import com.tianji.common.validate.annotations.EnumValid;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 枚举校验器校验逻辑
 *
 **/
@Slf4j
public class EnumValidator implements ConstraintValidator<EnumValid, BaseEnum> {

    private int[] enums = null;

    @Override
    public void initialize(EnumValid enumValid) {
        this.enums = enumValid.enumeration();
        log.info("payload>>{}",ArrayUtils.toString(enumValid.payload()));
    }

    @Override
    public boolean isValid(BaseEnum em, ConstraintValidatorContext context) {
        // 不做空校验
        if(em == null){
            return true;
        }
        //没有配置枚举值不校验
        if (ArrayUtils.isEmpty(enums)) {
            return true;
        }
        for (int e : enums) {
            if (e == em.getValue()) {
                return true;
            }
        }
        return false;
    }
}
