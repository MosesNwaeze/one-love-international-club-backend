package com.one_love_international_club.config;

import com.resend.Resend;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ResendConfiguration {

    @Value("${resend.api-key}")
    private String resendApiKey;

    @Bean
    public Resend resend(){
        return new Resend(resendApiKey);
    }
}
