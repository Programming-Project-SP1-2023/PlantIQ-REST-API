package com.plantiq.plantiqserver;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        System.out.println("[WebMvcConfig] Configured CORS");
        registry.addMapping("/**")
                .allowedMethods("*")
                .allowedOrigins("plantiq.azurewebsites.net")
                .allowedHeaders("*")
                .allowCredentials(false)
                .maxAge(-1);
    }
}
