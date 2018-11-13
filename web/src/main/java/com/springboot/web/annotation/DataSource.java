package com.springboot.web.annotation;

import com.springboot.web.config.druid.DataSourceContextHolder;

import java.lang.annotation.*;

/**
 * @program: My Study
 * @description: 动态数据源
 * @author: Leslie
 * @create: 2018-07-13 11:56
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface DataSource {
    String value() default DataSourceContextHolder.PRIMARY_DATA_SOURCE;
}
