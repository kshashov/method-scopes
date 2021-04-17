package io.github.kshashov.scopedmethods.integration.proxy.impl;

import io.github.kshashov.scopedmethods.api.ScopedMethod;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ScopedMethod(group = MyScopeConfiguration.SCOPE, key = "inner")
public @interface InnerScope {
}
