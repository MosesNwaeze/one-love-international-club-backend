package com.one_love_international_club.auth.entity;

import com.one_love_international_club.setting.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_login_2fa_tokens")
public class UserLogin2faTokenEntity extends BaseEntity {

    @Column(name = "user_id", nullable = false, columnDefinition = "UUID")
    @JdbcTypeCode(SqlTypes.UUID)
    private UUID userId;

    @Column(name = "token", nullable = false, columnDefinition = "UUID")
    @JdbcTypeCode(SqlTypes.UUID)
    private UUID token;

}

