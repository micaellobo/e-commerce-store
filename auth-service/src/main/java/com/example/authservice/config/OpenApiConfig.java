package com.example.authservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "Auth Service",
                description = "OpenAPI documentation for the Auth Service."
        ),
        servers = {
                @Server(
                        url = "http://localhost:8080",
                        description = "Local DEV"
                )
        }
)
public class OpenApiConfig {
}
