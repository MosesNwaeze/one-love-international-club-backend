package com.one_love_international_club.file_upload;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UploadRepository extends JpaRepository<UploadEntity, UUID> {
}
