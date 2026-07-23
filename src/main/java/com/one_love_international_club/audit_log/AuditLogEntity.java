package com.one_love_international_club.audit_log;

import com.one_love_international_club.enums.AuditAction;
import com.one_love_international_club.enums.AuditStatus;
import com.one_love_international_club.enums.OperationType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "audit_log")
@EntityListeners(AuditLogListener.class)
@Data
public class AuditLogEntity implements Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.UUID)
    @Column(columnDefinition = "UUID",
            updatable = false, nullable = false)
    private UUID id;

    @JdbcTypeCode(SqlTypes.UUID)
    @Column(columnDefinition = "UUID")
    private UUID userId;

    @Column(name = "username")
    private String username;

    @Column(name = "action")
    @Enumerated(EnumType.STRING)
    private AuditAction action;

    @Column(name = "entity_type")
    private String entityType;

    @Column(name = "entity_id")
    private UUID entityId;

    @Column(name = "old_value", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private String oldValue;

    @Column(name = "new_value", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private String newValue;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "user_agent")
    private String userAgent;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private AuditStatus status;

    @Column(name = "error_message", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private String errorMessage;

    @Column(name = "operation")
    @Enumerated(EnumType.STRING)
    private OperationType operation;

    @Column(name = "created_by", columnDefinition = "UUID")
    @JdbcTypeCode(SqlTypes.UUID)
    private UUID createdBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


}
