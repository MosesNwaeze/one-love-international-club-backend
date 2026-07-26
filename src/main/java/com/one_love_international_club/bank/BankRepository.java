package com.one_love_international_club.bank;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BankRepository extends JpaRepository<BankEntity, UUID> {
}
