package com.jiuzhang.url.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @auther: WZ
 * @Date: 2020/9/7 15:07
 * @Description:
 */
@EnableSwagger2
@Configuration
public class SwaggerConfig {

    @Bean
    public Docket webApiConfig(){

        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("webApi")
                .apiInfo(webApiInfo())
                .select()
                .build();
    }

    //基本信息的配置，信息会在api文档上显示
    private ApiInfo webApiInfo(){

        return new ApiInfoBuilder()
                .title("短网址")
                .description("本文档描述了短网址中接口定义")
                .version("1.0")
                .contact(new Contact("Helen","http://wenzhen.com","649575218@qq.com"))
                .build();
    }
}
