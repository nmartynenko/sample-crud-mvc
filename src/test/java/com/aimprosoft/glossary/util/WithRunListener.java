package com.aimprosoft.glossary.util;

import org.junit.runner.notification.RunListener;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface WithRunListener {
    Class<? extends RunListener> value();
}
