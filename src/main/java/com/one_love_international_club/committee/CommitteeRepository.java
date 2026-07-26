package com.one_love_international_club.committee;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CommitteeRepository extends JpaRepository<CommitteeEntity, UUID> {
}
