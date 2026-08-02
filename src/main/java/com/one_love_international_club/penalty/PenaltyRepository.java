package com.one_love_international_club.penalty;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface PenaltyRepository extends JpaRepository<PenaltyEntity, UUID> {
    @Query("""
            SELECT income FROM PenaltyEntity income
            WHERE income.paidBy = :userId
            """)
    Page<PenaltyEntity> findAllMyExpenses(UUID userId, Pageable pageable);
}
