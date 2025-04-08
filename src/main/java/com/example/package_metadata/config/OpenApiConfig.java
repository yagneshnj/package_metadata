package com.example.package_metadata.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("📦 Package Metadata API")
                        .version("1.0.0")
                        .description("API for managing package metadata including packages, licenses, and sources."));
    }
}

