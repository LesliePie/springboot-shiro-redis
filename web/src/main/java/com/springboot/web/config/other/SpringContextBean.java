package com.springboot.web.config.other;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @program: web
 * @description: 获取Spring上下文
 * @author: Leslie
 * @create: 2018-07-16 11:12
 **/
@Component
public class SpringContextBean implements ApplicationContextAware {

    private static ApplicationContext context=null;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context=applicationContext;
    }
    public static <T> T getBean(String name)
    {
        return (T)context.getBean(name);
    }

    public static <T> T getBean(Class<T> beanClass){
        return context.getBean(beanClass);
    }
    public static <T> T getBean(Class<T> clazz,String proper){
        if (clazz == null) {
            return null;
        }
        return context.getBean(proper,clazz);
    }
}
