package com.one_love_international_club.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface PostRepository extends JpaRepository<PostEntity, UUID> {

    @Query("""
            SELECT post.totalViewed FROM PostEntity post
            WHERE post.id = :postId
            """)
    Long getTotalViewed(UUID postId);
}
