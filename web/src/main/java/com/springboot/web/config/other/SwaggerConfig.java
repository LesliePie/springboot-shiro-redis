package com.springboot.web.config.other;

import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: web
 * @description: 文档插件swagger
 * @author: Leslie
 * @create: 2018-07-16 13:41
 **/
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    private static final String BASE_PACKAGE="com.education.controller";
    @Bean
    public Docket api(){
        ParameterBuilder parameterBuilder=new ParameterBuilder();
        parameterBuilder.parameterType("header")
                        .name("Authorization")
                        .description("header中Authorization字段用于认证")
                        .modelRef(new ModelRef("String"))
                        .required(false)
                        .build();
        List<Parameter> parameters=new ArrayList<>();
        parameters.add(parameterBuilder.build());
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("springboot")
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .build()
                .apiInfo(apiInfo1())
                .globalOperationParameters(parameters);
    }
    private ApiInfo apiInfo1() {
        return new ApiInfoBuilder()
                .title("springBoot API")
                .contact("王军,email:15198264880@163.com")
                .description("文档")
                .version("v0.02")
                .build();
    }
}
