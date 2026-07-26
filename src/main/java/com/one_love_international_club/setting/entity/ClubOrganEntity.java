package com.one_love_international_club.setting.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "club_organs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClubOrganEntity extends BaseEntity {
    @Column(name = "name", nullable = false)
    private String name;
}
