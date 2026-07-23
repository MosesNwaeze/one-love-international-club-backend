package com.one_love_international_club.util;

import com.one_love_international_club.auth.entity.UserLoginEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
public class AuditContext {
    // Getters and Setters
    private UserLoginEntity user;
    private String ipAddress;
    private String requestId;
    private String userAgent;
    private String sessionId;
    private Map<String, String> attributes = new HashMap<>();

    public String getAttribute(String key) {
        return attributes.get(key);
    }

    public void setAttribute(String key, String value) {
        attributes.put(key, value);
    }

    public Map<String, String> getAdditionalData() {
        return attributes;
    }
}