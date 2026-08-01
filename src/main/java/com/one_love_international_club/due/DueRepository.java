package com.one_love_international_club.due;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DueRepository extends JpaRepository<DueEntity, UUID> {
}
