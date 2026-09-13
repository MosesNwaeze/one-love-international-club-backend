package com.one_love_international_club.meeting;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MeetingRepository extends JpaRepository<MeetingEntity, UUID> {
}
