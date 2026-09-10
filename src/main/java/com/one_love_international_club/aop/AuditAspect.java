package com.one_love_international_club.aop;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.one_love_international_club.audit_log.AuditLogService;
import com.one_love_international_club.enums.AuditAction;
import com.one_love_international_club.enums.OperationType;
import com.one_love_international_club.security.SecurityService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Aspect
@RequiredArgsConstructor
public class AuditAspect {

    private final AuditLogService auditLogService;
    private final SecurityService securityService;
    private final ObjectMapper objectMapper;

    @Pointcut("@annotation(annotation)")
    public void pointCut(AuditAnnotation annotation) {
    }

    @AfterReturning(pointcut = "pointCut(annotation)", returning = "result", argNames = "joinPoint,annotation,result")
    public void createAudit(JoinPoint joinPoint, AuditAnnotation annotation, Object result) throws JsonProcessingException {

        AuditAction action = annotation.action();

        UUID entityId = UUID.fromString(annotation.entityId());

        OperationType operation = annotation.operation();


        String entityName = joinPoint.getClass().getName();

        String username = securityService.getCurrentUser().getEmail();


        auditLogService.logEntityChange(
                entityName,
                entityId,
                objectMapper.writeValueAsString(joinPoint.getTarget().toString()),
                null,
                action,
                username
        );


    }
}
