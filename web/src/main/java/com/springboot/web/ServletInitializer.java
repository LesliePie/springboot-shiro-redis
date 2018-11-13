package com.springboot.web;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * @program: springboot
 * @description: 用于打war包
 * @author: Leslie
 * @create: 2018-11-05 10:37
 **/
public class ServletInitializer extends SpringBootServletInitializer {
    //打war则取消以下注释
    /*  @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(WebApplication.class);
    }*/
}
