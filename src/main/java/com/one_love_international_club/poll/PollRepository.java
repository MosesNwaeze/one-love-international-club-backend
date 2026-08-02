package com.one_love_international_club.poll;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PollRepository extends JpaRepository<PollEntity, UUID> {
}
