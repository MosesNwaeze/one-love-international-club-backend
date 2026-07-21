package com.one_love_international_club.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class DotenvEnvironmentPostProcessor implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        try {
            Dotenv dotenv = Dotenv.configure()
                    .directory("./")
                    .ignoreIfMissing()
                    .load();

            Map<String, Object> propertySource = new HashMap<>();
            dotenv.entries().forEach(entry -> propertySource.put(entry.getKey(), entry.getValue()));

            environment.getPropertySources().addFirst(new MapPropertySource("dotenv", propertySource));

            System.out.println("Environment variables loaded from .env file via EnvironmentPostProcessor");
        } catch (Exception e) {
            System.err.println("Error loading .env file: " + e.getMessage());
        }
    }
}