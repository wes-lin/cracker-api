package com.mockst.cracker.validator;

import com.mockst.cracker.annotate.Phone;
import com.mockst.cracker.util.ValidateUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @Auther: zhiwei
 * @Date: 2019/10/20 20:48
 * @Description:
 */
public class PhoneValidator implements ConstraintValidator<Phone, String> {
    @Override
    public boolean isValid(String mobile, ConstraintValidatorContext constraintValidatorContext) {
        return ValidateUtils.validateMobile(mobile);
    }
}
