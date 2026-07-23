package com.one_love_international_club.util;

import com.one_love_international_club.dto.Response;
import com.one_love_international_club.dto.Status;
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
    public CompletableFuture<Response<Map<String, Object>>> sendEmail(String to, String subject, String body, Attachment attachment) {
        CreateEmailOptions params = CreateEmailOptions.builder()
                .from(from)
                .to(to)
                .subject(subject)
                .html(body)
                .addAttachment(attachment)
                .build();

        return sendEmail(params).toCompletableFuture();

    }

    @Async
    public CompletableFuture<Response<Map<String, Object>>> sendEmailWithTemplate(String templatePath, Map<String, Object> templateModel) {

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
        return sendEmail(params).toCompletableFuture();
    }


    private CompletionStage<Response<Map<String, Object>>> sendEmail(CreateEmailOptions options) {
        try {

            CreateEmailResponse response = resend.emails().send(options);

            Map<String, Object> map = new HashMap<>();
            map.put("id", response);

            log.info("Email send successfully, {}", response);

            return CompletableFuture.completedFuture(Response.<Map<String, Object>>builder()
                    .code(HttpStatus.OK.value())
                    .timestamp(LocalDateTime.now())
                    .status(Status.SUCCESSFUL)
                    .message("Email send successfully")
                    .data(map)
                    .build());

        } catch (Exception e) {
            log.error("Error sending email, {}", e.getMessage(), e);
            return CompletableFuture.completedFuture(Response.<Map<String, Object>>builder()
                    .message(e.getMessage())
                    .status(Status.ERROR)
                    .timestamp(LocalDateTime.now())
                    .build());
        }
    }

}
