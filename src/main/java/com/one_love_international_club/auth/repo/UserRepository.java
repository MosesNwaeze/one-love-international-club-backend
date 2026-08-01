package com.one_love_international_club.auth.repo;

import com.one_love_international_club.auth.entity.UserEntity;
import com.one_love_international_club.enums.ApprovalStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByEmail(String email);

    @Query("""
            SELECT DISTINCT COUNT (*) FROM UserEntity
            """)
    Long totalMembers();

    @Query("""
            SELECT users FROM UserEntity users
            WHERE users.approvalStatus = :approvalStatus
            ORDER BY users.createdAt, users.firstName, users.lastName
            """)
    Page<UserEntity> findAllUnapprovedUsers(@Param("approvalStatus") ApprovalStatus approvalStatus, Pageable pageable);
}