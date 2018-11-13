package com.springboot.web.aspect;

import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.Method;

/**
 * @program: web
 * @description:
 * @author: Leslie
 * @create: 2018-07-16 14:42
 **/
public interface AspectApi {
    Object doHandlerAspect(Object[] objects, ProceedingJoinPoint point, Method method, boolean isAll)throws Throwable;
}
