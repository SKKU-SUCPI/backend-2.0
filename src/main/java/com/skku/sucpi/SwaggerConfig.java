package com.skku.sucpi;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

import java.util.List;

@Configuration
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo())
                .servers(List.of(
                        new Server().url("http://siop-dev.skku.edu:8080"),
                        new Server().url("http://localhost:8080"),
                        new Server().url("https://siop-dev.skku.edu")
                ));
    }

    
 
    private Info apiInfo() {
        return new Info()
                .title("SUCPI API 명세서")
                .description("SUCPI 프로젝트의 API 설명 문서입니다.<br />Request Server는 하단에서 선택하실 수 있습니다.")
                .version("1.0.0");
    }
}

