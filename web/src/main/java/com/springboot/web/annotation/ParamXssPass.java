package com.springboot.web.annotation;

import java.lang.annotation.*;

/**
 * @program: web
 * @description: 防止XSS攻击
 * @author: Leslie
 * @create: 2018-07-16 15:08
 **/
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ParamXssPass {
}
