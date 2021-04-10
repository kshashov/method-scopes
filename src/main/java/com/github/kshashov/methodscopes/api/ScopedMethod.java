package com.github.kshashov.methodscopes.api;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface ScopedMethod {
    @AliasFor("key")
    String value() default "";

    @AliasFor("value")
    String key() default "";

    String group() default "";
}
