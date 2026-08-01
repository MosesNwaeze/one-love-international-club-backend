package com.one_love_international_club.due;

import com.one_love_international_club.setting.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "dues")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DueEntity extends BaseEntity {
    @Column(name = "name", nullable = false, unique = true)
    private String name;
}
