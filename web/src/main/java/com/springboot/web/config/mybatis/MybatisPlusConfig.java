package com.springboot.web.config.mybatis;

import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @program: web
 * @description: MybatisPlus配置
 * @author: Leslie
 * @create: 2018-07-13 17:33
 **/
@Configuration
@EnableTransactionManagement
@MapperScan("com.springboot.mapper")
public class MybatisPlusConfig {

    /**
    * @Description: 分页插件配置
    * @Param: []
    * @return: com.baomidou.mybatisplus.plugins.PaginationInterceptor
    * @Author: Leslie
    * @Date: 2018/7/13
    */
    @Bean
    public PaginationInterceptor paginationInterceptor(){
        PaginationInterceptor page = new PaginationInterceptor();
        page.setDialectType("MySQL");
        return page;
    }
}
