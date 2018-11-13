package com.springboot.web.aspect;

/**
 * @program: web
 * @description:
 * @author: Leslie
 * @create: 2018-07-16 15:41
 **/
public class ValidationParamOperate extends AspectHandler {
    @Override
    protected AspectApi factoryMehod() {
        return new ValidationParamAspect();
    }
}
