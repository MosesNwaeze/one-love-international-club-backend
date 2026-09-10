package com.one_love_international_club.aop;

import com.one_love_international_club.enums.AuditAction;
import com.one_love_international_club.enums.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AuditAnnotation {

    AuditAction action();

    String entityId();

    OperationType operation();
;
}
