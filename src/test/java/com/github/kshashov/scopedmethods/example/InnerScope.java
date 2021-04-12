package com.github.kshashov.scopedmethods.example;

import com.github.kshashov.scopedmethods.api.ScopedMethod;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ScopedMethod(group = MyScopeConfiguration.SCOPE, key = "inner")
public @interface InnerScope {
}
