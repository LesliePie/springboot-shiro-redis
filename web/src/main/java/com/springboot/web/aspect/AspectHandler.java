package com.springboot.web.aspect;

import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.Method;

/**
 * @program: web
 * @description: 切面处理
 * @author: Leslie
 * @create: 2018-07-16 14:51
 **/
public abstract class AspectHandler {
    /**
    * @Description: 完成注解功能
    * @Param: [point 切面类, objects 方法参数, method 方法, isAll 是否log和validation全验证]
    * @return: java.lang.Object
    * @Author: Leslie
    * @Date: 2018/7/16
    */
    public Object doAspectHandler(ProceedingJoinPoint point, Object[] objects, Method method,boolean isAll)throws  Throwable{
        AspectApi aspectApi=factoryMehod();
        return aspectApi.doHandlerAspect(objects,point,method,isAll);
    }

    protected abstract AspectApi factoryMehod();
}
