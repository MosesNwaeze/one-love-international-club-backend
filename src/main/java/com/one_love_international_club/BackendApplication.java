package com.one_love_international_club;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing
@EnableAsync
@EnableScheduling
@EnableAspectJAutoProxy
@EnableJpaRepositories
@EnableCaching
public class BackendApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(BackendApplication.class);
        application.setBannerMode(Banner.Mode.OFF);
        application.run(args);
    }
}
