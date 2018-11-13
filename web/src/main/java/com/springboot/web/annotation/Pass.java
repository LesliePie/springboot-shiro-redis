package com.springboot.web.annotation;

import java.lang.annotation.*;

/**
 * @program: web
 * @description: 放过token认证
 * @author: Leslie
 * @create: 2018-07-16 15:09
 **/
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Pass {
}
