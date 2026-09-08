package com.veltia.adaptivequiz.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "com.veltia.adaptivequiz")
@EnableJpaRepositories(basePackages = "com.veltia.adaptivequiz.infrastructure.persistence.jpa")
@EntityScan(basePackages = "com.veltia.adaptivequiz.infrastructure.persistence.entities")
public class AdaptiveQuizApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdaptiveQuizApplication.class, args);
    }
}
