package com.one_love_international_club.setting.repo;

import com.one_love_international_club.setting.entity.AppEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AppRepository extends JpaRepository<AppEntity, UUID> {

    Optional<AppEntity> findByNameIgnoreCase(String name);
}
