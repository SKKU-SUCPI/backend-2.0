package com.skku.sucpi;

import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

import java.util.List;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo())
                .servers(List.of(
                        new Server().url("http://localhost:8080"),
                        new Server().url("http://siop-dev.skku.edu:8080"),
                        new Server().url("https://siop-dev.skku.edu")
                ));
    }

    
 
    private Info apiInfo() {
        return new Info()
                .title("SUCPI API 명세서")
                .description("SUCPI 프로젝트의 API 설명 문서입니다")
                .version("1.0.0");
    }
}

