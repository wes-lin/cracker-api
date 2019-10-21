package com.mockst.cracker.annotate;

import java.lang.annotation.*;

/**
 * @author linzhiwei
 * @Description: json字符串转换
 * @date 2019/4/17 16:50
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JsonObjectParam {

    String name() default "";//参数名

}
