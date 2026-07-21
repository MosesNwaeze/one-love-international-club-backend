package com.one_love_international_club.auth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_login_2fa_tokens")
@IdClass(UserLogin2faTokenEntity.PK.class)
public class UserLogin2faTokenEntity {

    @Id
    @Column(name = "user_login_id", nullable = false)
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID userLoginId;

    @Id
    @Column(name = "token", length = 36, nullable = false)
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID token;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PK implements Serializable {
        private UUID userLoginId;
        private UUID token;
    }
}

