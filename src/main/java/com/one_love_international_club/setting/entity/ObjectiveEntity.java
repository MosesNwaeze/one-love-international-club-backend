package com.one_love_international_club.setting.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "objectives")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ObjectiveEntity extends BaseEntity {
    @Column(name = "title", nullable = false, columnDefinition = "TEXT")
    private String title;
}
