package io.github.kshashov.scopedmethods.integration.proxy.impl;

import io.github.kshashov.scopedmethods.api.ScopedMethod;
import io.github.kshashov.scopedmethods.integration.proxy.ProxyTestConfiguration;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ScopedMethod(group = ProxyTestConfiguration.SCOPE, key = "outer")
public @interface OuterScope {
}
