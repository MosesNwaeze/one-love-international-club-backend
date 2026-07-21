package com.api.vdtcommsws.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private static final String DEFAULT_EMAIL_URL = "http://41.216.160.37:9196/api/v1/vdt-workspace/notification/send-email";

    private final RestTemplate restTemplate;

    public void sendDefaultEmail(String body, String subject, String to) {

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("emailTo", to);
        requestBody.put("emailSubject", subject);
        requestBody.put("emailBody", body);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                DEFAULT_EMAIL_URL,
                HttpMethod.POST,
                requestEntity,
                String.class
            );
            log.info("Response Code: {}", response.getStatusCode());
            log.info("Response Body: {}", response.getBody());

        } catch (Exception e) {
            log.error("Email Service Error: {}", e.getMessage());
        }

    }

}
