package com.lth.identify_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.lth.identify_service")
public class IdentifyServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(IdentifyServiceApplication.class, args);
    }

}
