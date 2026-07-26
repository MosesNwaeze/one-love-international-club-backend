package com.one_love_international_club.auth.repo;

import com.one_love_international_club.auth.entity.UserEntity;
import com.one_love_international_club.setting.entity.UserLoginToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserLoginTokenRepository extends JpaRepository<UserLoginToken, UUID> {

    Optional<UserLoginToken> findByAccessKey(String accessKey);

    Optional<UserLoginToken> findByRefreshKey(String refreshKey);

    List<UserLoginToken> findAllByUserLoginAndExpiresAtAfter(UserEntity userEntity, LocalDateTime now);

    void deleteAllByUserLogin(UserEntity userEntity);

    void deleteAllByExpiresAtBefore(LocalDateTime now);
}