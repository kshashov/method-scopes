package io.github.kshashov.scopedmethods.integration.empty.impl;

import io.github.kshashov.scopedmethods.ScopedMethodsHolder;
import io.github.kshashov.scopedmethods.api.ScopedMethod;
import org.springframework.stereotype.Component;

@Component
public class Service {
    @ScopedMethod("inner")
    public void doSomething() {
        assert ScopedMethodsHolder.getCurrent() == null;
    }
}
