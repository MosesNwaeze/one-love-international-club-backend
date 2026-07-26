package com.one_love_international_club.benefits;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface BenefitRepository extends JpaRepository<BenefitEntity, UUID> {

    @Query("""
            SELECT user From UserEntity user
            WHERE user.firstName ILIKE CONCAT('%', :firstName, '%') OR
            user.lastName ILIKE CONCAT('%', :lastName, '%')
            ORDER BY user.createdAt DESC
            """)
    Page<BenefitEntity> findAllByUser(String firstName, String lastName, Pageable pageable);



}
