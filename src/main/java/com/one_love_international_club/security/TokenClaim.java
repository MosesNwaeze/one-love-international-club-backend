package com.one_love_international_club.security;

public enum TokenClaim {

    USERLOGIN_ID("userLoginId"),
    TENANT_ID("tenantId"),
    ACCESS_CATEGORY("accessCategory"),
    USERNAME("username"),
    ROLE("role"),
    TOKEN_TYPE("tokenType");

    private final String name;

    TokenClaim(String name) {

        this.name = name;
    }

    @Override
    public String toString() {

        return this.name;
    }
}
