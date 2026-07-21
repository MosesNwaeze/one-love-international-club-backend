package com.api.vdtcommsws.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.UUID;


@Getter
@Setter
@MappedSuperclass
@FilterDef(name = "notDeleted", defaultCondition = "deleted_at IS NULL")
public abstract class BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @JdbcTypeCode(SqlTypes.CHAR)
  @Column(nullable = false, updatable = false, columnDefinition = "CHAR(36)")
  private UUID id;

  @CreatedDate
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @LastModifiedDate
  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  @Column(name = "deleted_at")
  private LocalDateTime deletedAt;

  @CreatedBy
  @Column(name = "created_by_id")
  @JdbcTypeCode(SqlTypes.CHAR)
  private UUID createdById;

  @Column(name = "updated_by_id")
  @JdbcTypeCode(SqlTypes.CHAR)
  private UUID updatedById;

  @Column(name = "deleted_by_id")
  @JdbcTypeCode(SqlTypes.CHAR)
  private UUID deletedById;

  @Column(name = "restored_by_id")
  @JdbcTypeCode(SqlTypes.CHAR)
  private UUID restoredById;

  @PrePersist
  protected void onCreate() {
    this.createdAt = LocalDateTime.now();
    this.updatedAt = LocalDateTime.now();
  }

}
