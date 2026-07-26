package com.one_love_international_club.setting.repo;

import com.one_love_international_club.setting.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {
}
