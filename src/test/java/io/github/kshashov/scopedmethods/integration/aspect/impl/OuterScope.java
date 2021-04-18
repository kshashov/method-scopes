package io.github.kshashov.scopedmethods.integration.aspect.impl;

import io.github.kshashov.scopedmethods.api.ScopedMethod;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ScopedMethod(group = "mygroup", key = "outer")
public @interface OuterScope {
}
