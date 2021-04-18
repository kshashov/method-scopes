package io.github.kshashov.scopedmethods.integration.aspect.impl;

import io.github.kshashov.scopedmethods.api.ScopedMethod;
import io.github.kshashov.scopedmethods.integration.aspect.AspectTestConfiguration;

public abstract class IService {
    @ScopedMethod(group = AspectTestConfiguration.SCOPE, key = "outer")
    public abstract void doSomething();
}
