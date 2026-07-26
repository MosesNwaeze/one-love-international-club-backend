package com.one_love_international_club.signatory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface SignatoryRepository extends JpaRepository<SignatoryEntity, UUID> {
    @Query("""
            SELECT sig FROM SignatoryEntity sig 
            JOIN sig.signatory user
            WHERE user.lastName ILIKE CONCAT('%', :lastName, '%') AND 
            user.firstName ILIKE CONCAT('%', :firstName, '%') 
            ORDER BY sig.createdAt DESC 
            LIMIT 1                     
            """)
    Optional<SignatoryEntity> findByUsername(String lastName, String firstName);

}
