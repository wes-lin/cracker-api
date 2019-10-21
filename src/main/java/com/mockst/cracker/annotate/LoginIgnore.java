package com.mockst.cracker.annotate;

import java.lang.annotation.*;

/**
 * @describe: 忽略登录拦截
 * @author: linzhiwei
 * @date: 2018/11/23 9:39
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LoginIgnore {
}
