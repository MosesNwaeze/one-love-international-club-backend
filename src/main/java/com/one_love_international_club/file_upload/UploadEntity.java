package com.one_love_international_club.file_upload;

import com.one_love_international_club.auth.entity.UserEntity;
import com.one_love_international_club.setting.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "documents")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UploadEntity extends BaseEntity {

    @Column(name = "file", nullable = false)
    private String file;

    @Column(name = "file_public_id", nullable = false)
    private String filePublicId;

    @Column(name = "category", nullable = false)
    private String category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JdbcTypeCode(SqlTypes.UUID)
    private UserEntity uploadedBy;
}
