package io.github.kshashov.scopedmethods.integration.aspect.impl;

import io.github.kshashov.scopedmethods.ScopedMethodsHolder;
import io.github.kshashov.scopedmethods.api.ScopedMethod;
import io.github.kshashov.scopedmethods.integration.aspect.AspectTestConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class Service2 {

    @Autowired
    Environment environment;

    @ScopedMethod(group = AspectTestConfiguration.SCOPE, key = "inner")
    public void doSomething() {
        assert ScopedMethodsHolder.getCurrent(AspectTestConfiguration.SCOPE).equals("inner");
    }
}
