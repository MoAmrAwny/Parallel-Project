package com.jana.pp4.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/*") // Allow all endpoints
                        .allowedOrigins("http://localhost:5173") // React's default port
                        .allowedMethods("GET", "POST", "PUT", "DELETE")
                        .allowedHeaders("");
            }
        };
    }
}