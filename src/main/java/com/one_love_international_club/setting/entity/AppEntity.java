package com.one_love_international_club.setting.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "apps")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppEntity extends BaseEntity {

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "headquarter", nullable = false, unique = true)
    private String headquarters;

    @Column(name = "motto", nullable = false, unique = true)
    private String motto;

}
