package com.one_love_international_club.expenses;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface ExpensesRepository extends JpaRepository<ExpensesEntity, UUID> {
    @Query("""
            SELECT expense FROM ExpensesEntity expense
            WHERE expense.receiver = :userId
            ORDER BY expense.createdAt DESC
            """)
    Page<ExpensesEntity> findAllMyBenefits(UUID userId, Pageable pageable);
}
