package com.one_love_international_club.audit_log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.one_love_international_club.enums.AuditAction;
import com.one_love_international_club.enums.AuditStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuditLogService {
    private final AuditLogRepository auditLogRepository;

    private final ObjectMapper objectMapper;

    public void logAction(AuditLogEntity auditLog) {
        try {
            auditLogRepository.save(auditLog);
            log.debug("Audit log saved: {}", auditLog.getAction());
        } catch (Exception e) {
            log.error("Failed to save audit log", e);
        }
    }

    public void logEntityChange(String entityType, UUID entityId,
                                Object oldValue, Object newValue,
                                AuditAction action, String username) {
        AuditLogEntity auditLog = new AuditLogEntity();
        auditLog.setEntityType(entityType);
        auditLog.setEntityId(entityId);
        auditLog.setAction(action);
        auditLog.setUsername(username);
        auditLog.setOldValue(objectToJson(oldValue));
        auditLog.setNewValue(objectToJson(newValue));
        auditLog.setCreatedAt(LocalDateTime.now());
        auditLog.setStatus(AuditStatus.SUCCESS);

        logAction(auditLog);
    }

    public void logEntityChange(AuditLogEntity auditLog) {

        AuditLogEntity auditLogEntity = new AuditLogEntity();
        auditLogEntity.setUpdatedAt(LocalDateTime.now());
        auditLogEntity.setAction(AuditAction.UPDATE);
        auditLogEntity.setNewValue(auditLog.getUsername());
    }

    private String objectToJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return obj != null ? obj.toString() : null;
        }
    }
}
