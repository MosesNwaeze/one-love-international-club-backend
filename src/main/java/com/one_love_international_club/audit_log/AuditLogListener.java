package com.one_love_international_club.audit_log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.one_love_international_club.auth.entity.UserEntity;
import com.one_love_international_club.enums.AuditAction;
import com.one_love_international_club.enums.OperationType;
import com.one_love_international_club.util.AuditContextHolder;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuditLogListener {

    private final ObjectMapper objectMapper;
    private final AuditLogService auditLogService;

    @PrePersist
    public void beforePersist(Object entity) {
        if (entity instanceof Auditable auditable) {

            AuditLogEntity auditLogEntity = new AuditLogEntity();
            auditLogEntity.setNewValue(auditLogService.objectToJson(auditable.getInstance()));
            auditLogEntity.setCreatedAt(LocalDateTime.now());
            auditLogEntity.setEntityType(auditable.getClass().getSimpleName());
            auditLogEntity.setEntityId(auditable.getId());
            auditLogEntity.setAction(AuditAction.CREATE);
            auditLogEntity.setIpAddress(AuditContextHolder.getIpAddress());
            auditLogEntity.setOperation(OperationType.INSERT);
            auditLogEntity.setUserId(AuditContextHolder.getCurrentUser().getId());
            auditLogEntity.setUsername(AuditContextHolder.getCurrentUser().getEmail());

            auditLogService.logAction(auditLogEntity);

        }
    }

    @PreUpdate
    public void beforeUpdate(Object entity) {
        if (entity instanceof Auditable auditable) {
           Auditable oldValue = auditable.getInstance();

        }
    }

    @PreRemove
    public void beforeRemove(Object entity) {
        if (entity instanceof Auditable) {
            // Log deletion
            logDeletion(entity);
        }
    }

    private UserEntity getCurrentUser() {
        return AuditContextHolder.getCurrentUser();
    }

    private void logDeletion(Object entity) {
        // Log deletion details
    }

}
