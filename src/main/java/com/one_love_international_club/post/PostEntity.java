package com.one_love_international_club.post;

import com.one_love_international_club.auth.entity.UserEntity;
import com.one_love_international_club.enums.PostType;
import com.one_love_international_club.setting.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "posts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostEntity extends BaseEntity {

    @Column(name = "title", nullable = false, unique = true)
    private String title;

    @Column(name = "post_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private PostType postType = PostType.NEWS;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "post_image")
    private String postImage;

    @Column(name = "post_image_public_id")
    private String postPublicId;

    @Column(name = "total_viewed", nullable = false)
    private Integer totalViewed = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", columnDefinition = "UUID")
    @JdbcTypeCode(SqlTypes.UUID)
    private UserEntity createdBy;
}
