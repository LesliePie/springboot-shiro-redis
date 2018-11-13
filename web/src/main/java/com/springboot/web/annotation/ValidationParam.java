package com.springboot.web.annotation;

import java.lang.annotation.*;

/**
 * @program: web
 * @description:
 * @author: Leslie
 * @create: 2018-07-16 15:10
 **/

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ValidationParam {
    String value() default "";
}
