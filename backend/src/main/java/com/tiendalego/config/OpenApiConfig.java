package com.tiendalego.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI tiendaLegoOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Tienda LEGO API")
                        .description("API REST para la tienda de sets LEGO")
                        .version("1.0.0"));
    }
}
