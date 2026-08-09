package com.one_love_international_club.auth.roles;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminRole extends BaseRole {
    private Integer activeMembers;
    private Integer pendingMembers;
    private Integer executiveMembers;
}
