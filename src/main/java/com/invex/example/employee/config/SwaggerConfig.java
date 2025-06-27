package com.invex.example.employee.config;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    
    @Bean
    GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("Employee-Management")
                .pathsToMatch("/employees/**")
                .build();
    }
}
