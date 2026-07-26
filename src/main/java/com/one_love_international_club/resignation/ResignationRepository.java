package com.one_love_international_club.resignation;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ResignationRepository extends JpaRepository<ResignationEntity, UUID> {
}
