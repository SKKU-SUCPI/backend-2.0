package com.skku.sucpi;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
 
@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo());
    }

    
 
    private Info apiInfo() {
        return new Info()
                .title("SUCPI API 명세서")
                .description("SUCPI 프로젝트의 API 설명 문서입니다")
                .version("1.0.0");
    }
}

