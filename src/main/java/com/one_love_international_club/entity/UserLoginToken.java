package com.one_love_international_club.entity;


import com.one_love_international_club.auth.entity.UserLoginEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "user_login_tokens")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginToken {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(nullable = false, updatable = false, columnDefinition = "CHAR(36)")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_login_id", nullable = false)
    @JdbcTypeCode(SqlTypes.CHAR)
    private UserLoginEntity userLogin;

    @Column(name = "access_key")
    private String accessKey;

    @Column(name = "refresh_key")
    private String refreshKey;

    @Column(name = "token_type")
    private String tokenType;

    private String scope;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }
}