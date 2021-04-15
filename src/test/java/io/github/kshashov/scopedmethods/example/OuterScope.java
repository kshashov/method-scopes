package io.github.kshashov.scopedmethods.example;

import io.github.kshashov.scopedmethods.api.ScopedMethod;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ScopedMethod(group = MyScopeConfiguration.SCOPE, key = "outer")
public @interface OuterScope {
}
