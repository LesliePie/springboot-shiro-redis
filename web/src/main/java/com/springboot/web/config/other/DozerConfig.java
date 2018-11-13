package com.springboot.web.config.other;

import org.dozer.DozerBeanMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: education-parent
 * @description: 类转换dozer
 * @author: Leslie
 * @create: 2018-08-02 15:20
 **/
@Configuration
public class DozerConfig {


    @Bean
    public DozerBeanMapper mapper(){
        DozerBeanMapper dozerBeanMapper=new DozerBeanMapper();
        return dozerBeanMapper;
    }
}
