package com.aimprosoft.spring.validation.oval.spring;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface SpringValidateNestedProperty {
}