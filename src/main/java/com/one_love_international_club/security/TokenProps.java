package com.one_love_international_club.security;

import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record TokenProps(String username, UUID id, List<String> roles) {
}
