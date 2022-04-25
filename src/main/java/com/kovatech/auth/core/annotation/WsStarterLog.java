package com.kovatech.auth.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface WsStarterLog {
    String level() default "INFO";

    String process() default "DxL Starter";

    String message() default "Consider disabling starter logging annotation";
}