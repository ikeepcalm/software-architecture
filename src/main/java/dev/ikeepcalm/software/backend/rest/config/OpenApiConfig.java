package dev.ikeepcalm.software.backend.rest.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Equipment Repair Web Service API")
                        .version("1.0.0"))
                .servers(Collections.singletonList(
                        new Server().url("http://localhost:8080/v1").description("Local Server")
                ));
    }
}