package com.springboot.web.annotation;

import java.lang.annotation.*;

/**
 * @program: web
 * @description: 日志注解
 * @author: Leslie
 * @create: 2018-07-16 15:05
 **/
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {
    /**
     * 描述
     * @return
     */
    String description() default "";
}
