package com.springboot.web.config.other;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.MultipartConfigElement;

/**
 * @program: parent
 * @description: upload
 * @author: Leslie
 * @create: 2018-11-07 16:53
 **/
@Configuration
public class UploadConfig {
    @Value(value = "${file-upload.max-size}")
    private String maxSize;
    @Value(value = "${file-upload.request-size}")
    private String requestSize;
    @Value(value = "${file-upload.location}")
    private String location;

    /**
     * 文件上传配置
     * @return
     */
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        // 单个文件最大
        factory.setMaxFileSize(maxSize);
        // 设置总上传数据总大小
        factory.setMaxRequestSize(requestSize);
        //暂存区
        factory.setLocation(location);
        return factory.createMultipartConfig();
    }
}
