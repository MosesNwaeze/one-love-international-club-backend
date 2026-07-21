package com.one_love_international_club.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

@Configuration
public class DotenvConfig {

    @PostConstruct
    public void loadEnv() {
        try {
            Dotenv dotenv = Dotenv.configure()
                    .directory("./")  // Directory where .env file is located
                    .ignoreIfMissing()  // Don't throw exception if file is missing
                    .load();

            // Load all environment variables from .env file
            dotenv.entries().forEach(entry -> {
                if (System.getenv(entry.getKey()) == null) {
                    System.setProperty(entry.getKey(), entry.getValue());
                }
            });

            System.out.println("Environment variables loaded from .env file");
        } catch (Exception e) {
            System.err.println("Error loading .env file: " + e.getMessage());
        }
    }
}