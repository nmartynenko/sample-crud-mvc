package com.aimprosoft.contrib.spring.oval;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface SpringValidateNestedProperty {
}