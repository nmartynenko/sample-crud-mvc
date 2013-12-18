package com.aimprosoft.glossary.util;

import com.aimprosoft.glossary.common.model.UserRole;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface InvokeAs {
    UserRole value();
}