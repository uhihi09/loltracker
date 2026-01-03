package com.uhihi.loltracker.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Value("${spring.application.name}")
    private String applicationName;

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(apiInfo())
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Local Server"),
                        new Server().url("https://api.loltracker.com").description("Production Server")
                ));
    }

    private Info apiInfo() {
        return new Info()
                .title("LOL Custom Tracker API")
                .description("친구들과의 리그오브레전드 내전을 자동으로 기록하는 웹 애플리케이션 API")
                .version("v1.0.0")
                .contact(new Contact()
                        .name("uhihi")
                        .email("contact@loltracker.com")
                        .url("https://github.com/uhihi/lol-custom-tracker"))
                .license(new License()
                        .name("Apache License 2.0")
                        .url("https://www.apache.org/licenses/LICENSE-2.0"));
    }
}