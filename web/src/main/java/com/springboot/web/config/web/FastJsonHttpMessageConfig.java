package com.springboot.web.config.web;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;

/**
 * @program: web
 * @description: j
 * @author: Leslie
 * @create: 2018-07-16 10:43
 **/
public class FastJsonHttpMessageConfig extends FastJsonHttpMessageConverter {

    public FastJsonHttpMessageConfig() {

    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return super.supports(clazz);
    }
}
