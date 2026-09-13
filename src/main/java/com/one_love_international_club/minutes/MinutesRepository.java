package com.one_love_international_club.minutes;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MinutesRepository extends JpaRepository<MinutesEntity, UUID> {
}
