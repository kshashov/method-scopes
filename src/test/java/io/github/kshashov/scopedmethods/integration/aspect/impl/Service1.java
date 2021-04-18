package io.github.kshashov.scopedmethods.integration.aspect.impl;

import io.github.kshashov.scopedmethods.ScopedMethodsHolder;
import io.github.kshashov.scopedmethods.api.ScopedMethod;
import io.github.kshashov.scopedmethods.integration.aspect.AspectTestConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Service1 extends IService {
    @Autowired
    Service2 service2;

    @Override
    public void doSomething() {
        // Not working with overriden methods
        assert ScopedMethodsHolder.getCurrent(AspectTestConfiguration.SCOPE) == null;
    }

    @OuterScope
    public void doSomething2() {
        // Not working with meta annotations
        assert ScopedMethodsHolder.getCurrent(AspectTestConfiguration.SCOPE) == null;
    }

    @ScopedMethod(group = AspectTestConfiguration.SCOPE, key = "outer")
    public void doSomething3() {
        assert ScopedMethodsHolder.getCurrent(AspectTestConfiguration.SCOPE).equals("outer");
        service2.doSomething();
        assert ScopedMethodsHolder.getCurrent(AspectTestConfiguration.SCOPE).equals("outer");
    }
}
