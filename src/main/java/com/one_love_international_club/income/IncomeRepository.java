package com.one_love_international_club.income;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface IncomeRepository extends JpaRepository<IncomeEntity, UUID> {
    @Query("""
            SELECT income FROM IncomeEntity  income
            WHERE income.paidBy = :userId
            """)
    Page<IncomeEntity> findAllMyExpenses(UUID userId, Pageable pageable);
}
