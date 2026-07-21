package com.one_love_international_club.auth.repo;

import com.one_love_international_club.auth.entity.UserLoginEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserLoginRepository extends JpaRepository<UserLoginEntity, UUID> {
    Optional<UserLoginEntity> findByEmail(String email);

    boolean existsByEmail(String email);
}