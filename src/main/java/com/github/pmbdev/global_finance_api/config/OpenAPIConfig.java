package com.github.pmbdev.global_finance_api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                // Error in Railway HTTPS -> HTTP
                .addServersItem(new Server().url("/").description("Default Server URL"))

                .info(new Info()
                        .title("Global Finance API")
                        .version("1.0.0")
                        .description("Financial Banking API")
                        .contact(new Contact()
                                .name("Pablo Matanza Barahona")
                                .email("pablo.matbar@gmail.com")
                                .url("https://github.com/pmb-dev")))

                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Enter JWT token")))

                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }
}