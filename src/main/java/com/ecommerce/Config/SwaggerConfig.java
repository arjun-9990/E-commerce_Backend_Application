package com.ecommerce.Config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenApi(){
        SecurityScheme bearerScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .description("JWT Bearer Token");

        SecurityRequirement bearerRequriement = new SecurityRequirement()
                .addList("Bearer Authentication");

        return new OpenAPI()
                .info(new Info()
                        .title("Spring Boot E-commerce API")
                        .version("3.5.7")
                        .description("This is a spring boot project for E-commerce")
                        .license(new License().name("Apache 2.0").url("https://github.com/arjun-9990/E-commerce_Backend_Application.git"))
                        .contact(new Contact().name("Arjun Gaikwad").email("arjungaikwad9990@gmail.com").url("www.linkedin.com/in/arjungaikwad"))
                ).externalDocs(new ExternalDocumentation()
                        .description("Project Documentation")
                        .url("https://github.com/arjun-9990/E-commerce_Backend_Application.git")
                )
                .components(new Components().addSecuritySchemes("Bearer Authentication",bearerScheme))
                .addSecurityItem(bearerRequriement);

    }


}
