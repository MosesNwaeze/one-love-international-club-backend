package com.one_love_international_club.util;

import com.one_love_international_club.setting.dto.Response;
import com.one_love_international_club.setting.dto.Status;
import com.resend.Resend;
import com.resend.services.emails.model.Attachment;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    @Value("${resend.from}")
    private String from;

    private final Resend resend;
    private final TemplateEngine templateEngine;

    @Async
    public void sendEmail(String to, String subject, String body) {
        CreateEmailOptions params = CreateEmailOptions.builder()
                .from(from)
                .to(to)
                .subject(subject)
                .html(body)
                .build();

        send(params);

    }

    @Async
    public void sendEmailWithAttachment(String to, String subject, String body, Attachment attachment){
        CreateEmailOptions params = CreateEmailOptions.builder()
                .from(from)
                .to(to)
                .subject(subject)
                .html(body)
                .addAttachment(attachment)
                .build();

        send(params);

    }

    @Async
    public void sendEmailWithTemplate(String templatePath, Map<String, Object> templateModel) {

        Context context = new Context();
        context.setVariables(templateModel);

        String process = templateEngine.process(templatePath, context);

        CreateEmailOptions params = CreateEmailOptions.builder()
                .from(from)
                .to((String) templateModel.get("to"))
                .subject((String) templateModel.get("subject"))
                .html(process)
                .addAttachment((Attachment) templateModel.get("attachment"))
                .build();
        send(params);
    }


    private void send(CreateEmailOptions options) {
        try {

            CreateEmailResponse response = resend.emails().send(options);

            log.info("Email send successfully, {}", response);


        } catch (Exception e) {
            log.error("Error sending email, {}", e.getMessage(), e);
        }
    }

}
