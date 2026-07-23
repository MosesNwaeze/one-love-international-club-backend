package com.one_love_international_club.audit_log;

import com.one_love_international_club.auth.entity.UserLoginEntity;

import java.time.LocalDateTime;
import java.util.UUID;

public interface Auditable {
    UUID getId();

    void setUpdatedAt(LocalDateTime updatedAt);
}