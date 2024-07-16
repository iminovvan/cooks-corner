package com.example.cooks_corner.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Nargiza",
                        email = "nargizh03@gmail.com"
                ),
                title = "CooksCorner",
                description = "API documentation for authentication project",
                version = "0.0.1"
        ),
        servers = {
                @Server(
                        description = "Railway Server",
                        url = "https://cooks-corner-prod.up.railway.app/"
                )
        }
)
public class OpenApiConfig {
}
