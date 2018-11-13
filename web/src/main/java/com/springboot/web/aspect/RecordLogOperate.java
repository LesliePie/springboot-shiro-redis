package com.springboot.web.aspect;

/**
 * @program: web
 * @description:
 * @author: Leslie
 * @create: 2018-07-16 15:33
 **/
public class RecordLogOperate extends AspectHandler {



    @Override
    protected AspectApi factoryMehod() {
        return new RecordLogAspect();
    }
}
