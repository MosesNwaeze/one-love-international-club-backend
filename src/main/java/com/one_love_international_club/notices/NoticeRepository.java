package com.one_love_international_club.notices;

import com.one_love_international_club.enums.Audience;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface NoticeRepository extends JpaRepository<NoticeEntity, UUID> {

    @Query("""
            SELECT notice FROM NoticeEntity notice
            WHERE notice.audience = :audience
            ORDER BY notice.createdAt DESC 
            """)
    Page<NoticeEntity> findAllNoticesByAudience(@Param("audience")Audience audience,  Pageable pageable);
}
