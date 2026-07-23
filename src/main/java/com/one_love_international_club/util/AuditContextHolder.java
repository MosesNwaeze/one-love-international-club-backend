package com.one_love_international_club.util;

import com.one_love_international_club.auth.entity.UserLoginEntity;

import java.util.*;

public class AuditContextHolder {

    private static final ThreadLocal<Map<String, Object>> CONTEXT =
            ThreadLocal.withInitial(HashMap::new);

    private static final ThreadLocal<UserLoginEntity> CURRENT_USER = new ThreadLocal<>();

    private static final ThreadLocal<String> IP_ADDRESS = new ThreadLocal<>();

    private static final ThreadLocal<String> REQUEST_ID = new ThreadLocal<>();

    public static void setCurrentUser(UserLoginEntity user) {
        CURRENT_USER.set(user);
    }

    public static UserLoginEntity getCurrentUser() {
        return CURRENT_USER.get();
    }

    public static void setIpAddress(String ip) {
        IP_ADDRESS.set(ip);
    }

    public static String getIpAddress() {
        return IP_ADDRESS.get();
    }

    public static void setRequestId(String requestId) {
        REQUEST_ID.set(requestId);
    }

    public static String getRequestId() {
        return REQUEST_ID.get();
    }

    public static void setAttribute(String key, Object value) {
        CONTEXT.get().put(key, value);
    }

    public static Object getAttribute(String key) {
        return CONTEXT.get().get(key);
    }

    public static void clear() {
        CURRENT_USER.remove();
        IP_ADDRESS.remove();
        REQUEST_ID.remove();
        CONTEXT.remove();
    }

    public static void setContext(AuditContext context) {
        setCurrentUser(context.getUser());
        setIpAddress(context.getIpAddress());
        setRequestId(context.getRequestId());
        if (context.getAdditionalData() != null) {
            context.getAdditionalData().forEach(AuditContextHolder::setAttribute
            );
        }
    }

    public static AuditContext getContext() {
        AuditContext context = new AuditContext();
        context.setUser(getCurrentUser());
        context.setIpAddress(getIpAddress());
        context.setRequestId(getRequestId());
        return context;
    }

}
