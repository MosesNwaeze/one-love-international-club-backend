package com.one_love_international_club.audit_log;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuditLogListener {

//    private final AuditLogService auditLogService;
//    private final SecurityService securityService;
//    private final ObjectMapper objectMapper;
//
//    @PrePersist
//    public void beforePersist(Object entity) {
//        if (entity instanceof Auditable auditable) {
//            auditLogService.logEntityChange(
//                    auditable.getClass().getName(),
//                    auditable.getId(),
//                    null,
//                    toJson(auditable),
//                    AuditAction.CREATE,
//                    AuditContextHolder.getCurrentUser().getEmail()
//            );
//
//        }
//    }
//
//    @PreUpdate
//    public void beforeUpdate(Object entity) {
//        if (entity instanceof Auditable auditable) {
//
//        }
//    }
//
//    @PreRemove
//    public void beforeRemove(Object entity) {
//        if (entity instanceof Auditable) {
//            // Log deletion
//            logDeletion(entity);
//        }
//    }
//
//    private UserLoginEntity getCurrentUser() {
//        return AuditContextHolder.getCurrentUser();
//    }
//
//    private void logDeletion(Object entity) {
//        // Log deletion details
//    }
//
//
//    private String toJson(Object entity) {
//        try {
//            return objectMapper.writeValueAsString(entity);
//        } catch (JsonProcessingException e) {
//            log.error("Error serializing entity", e);
//            return "";
//        }
//    }


}
