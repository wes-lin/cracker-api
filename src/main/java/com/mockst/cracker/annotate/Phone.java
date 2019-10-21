package com.mockst.cracker.annotate;

import com.mockst.cracker.validator.PhoneValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Auther: zhiwei
 * @Date: 2019/10/20 20:45
 * @Description: 手机号检验
 */
@Target({ ElementType.FIELD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PhoneValidator.class)
public @interface Phone {
    String message() default "请输入正确手机号";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
