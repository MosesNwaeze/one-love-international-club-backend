package com.one_love_international_club.auth.repo;

import com.one_love_international_club.auth.entity.UserEntity;
import com.one_love_international_club.enums.ApprovalStatus;
import com.one_love_international_club.setting.dto.Status;
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
    Optional<UserEntity> findByEmailIgnoreCase(String email);

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

    @Query("""
                SELECT DISTINCT users FROM UserEntity users
                LEFT JOIN users.roleEntity role
                LEFT JOIN role.clubOrgan clubOrgan
                WHERE(
                (:search IS NULL OR
                :search = '' OR
                LOWER(users.firstName) LIKE LOWER(CONCAT('%', :search, '%')) OR
                LOWER(users.lastName) LIKE LOWER(CONCAT('%', :search, '%')) OR
                LOWER(users.email) LIKE LOWER(CONCAT('%', :search, '%')) OR
                LOWER(role.name) LIKE LOWER(CONCAT('%', :search, '%')) OR
                LOWER(clubOrgan.name) LIKE LOWER(CONCAT('%', :search, '%'))) AND
                 users.status = :status
                )
                ORDER BY users.lastName ASC, users.firstName ASC
            """)
    Page<UserEntity> findUsers(
            @Param("status") Status status,
            @Param("search") String search,
            Pageable pageable);

    @Query("""
            SELECT DISTINCT users FROM UserEntity users
            JOIN FETCH users.roleEntity role
            JOIN FETCH role.clubOrgan clubOrgan
            WHERE clubOrgan.name = :name
            AND (
                :search IS NULL OR
                :search = '' OR
                LOWER(users.lastName) LIKE LOWER(CONCAT('%', :search, '%')) OR
                LOWER(users.firstName) LIKE LOWER(CONCAT('%', :search, '%'))
            )
            ORDER BY users.lastName ASC, users.firstName ASC
            """)
    Page<UserEntity> findExecutiveUsers(
            @Param("name") String name,
            @Param("search") String search,
            Pageable pageable);


    @Query("""
            SELECT DISTINCT users FROM UserEntity users
            LEFT JOIN users.roleEntity role
            LEFT JOIN role.clubOrgan clubOrgan
            WHERE (
                :search IS NULL OR
                :search = '' OR
                LOWER(users.firstName) LIKE LOWER(CONCAT('%', :search, '%')) OR
                LOWER(users.lastName) LIKE LOWER(CONCAT('%', :search, '%')) OR
                LOWER(users.email) LIKE LOWER(CONCAT('%', :search, '%')) OR
                LOWER(role.name) LIKE LOWER(CONCAT('%', :search, '%')) OR
                LOWER(clubOrgan.name) LIKE LOWER(CONCAT('%', :search, '%')) OR
                 (
                    :approvalStatus IS NOT NULL AND
                    users.approvalStatus = :approvalStatus
                 )
            )
            ORDER BY users.lastName ASC, users.firstName ASC
            """)
    Page<UserEntity> findAllUsers(
            @Param("search") String search,
            @Param("approvalStatus") ApprovalStatus approvalStatus,
            Pageable pageable);
}