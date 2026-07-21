package com.one_love_international_club.auth.repo;

import com.one_love_international_club.auth.entity.UserLoginEntity;
import com.one_love_international_club.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, UUID> {

    Optional<PasswordResetToken> findByToken(String token);

    Optional<PasswordResetToken> findByUserLogin(UserLoginEntity userLoginEntity);

    void deleteByExpiryDateBefore(LocalDateTime now);
}