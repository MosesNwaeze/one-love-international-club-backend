package com.one_love_international_club.auth.repo;

import com.one_love_international_club.auth.entity.UserLogin2faTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserLogin2faTokenRepository extends JpaRepository<UserLogin2faTokenEntity, UUID> {

    boolean existsByUserLoginIdAndToken(UUID userLoginId, UUID token);

    void deleteByUserLoginIdAndToken(UUID userLoginId, UUID token);
}

