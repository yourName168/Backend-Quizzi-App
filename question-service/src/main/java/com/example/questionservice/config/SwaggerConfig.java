package com.example.questionservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    
    @Bean
    public OpenAPI questionServiceAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Question Service API")
                        .description("API documentation for Question Service")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Quiz App Team")
                                .email("support@quizapp.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://www.apache.org/licenses/LICENSE-2.0.html")));
    }
}