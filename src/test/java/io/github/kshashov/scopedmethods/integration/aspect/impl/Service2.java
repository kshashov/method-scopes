package io.github.kshashov.scopedmethods.integration.aspect.impl;

import io.github.kshashov.scopedmethods.ScopedMethodsHolder;
import io.github.kshashov.scopedmethods.api.ScopedMethod;
import io.github.kshashov.scopedmethods.integration.aspect.AspectTestConfiguration;
import io.github.kshashov.scopedmethods.integration.proxy.ProxyTestConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class Service2 {

    @Autowired
    Environment environment;

    @ScopedMethod(group = AspectTestConfiguration.SCOPE, key = "inner")
    @ScopedMethod(group = ProxyTestConfiguration.SCOPE + "2", key = "inner5")
    @ScopedMethod(group = ProxyTestConfiguration.SCOPE + "2", key = "inner7")
    @ScopedMethod(group = ProxyTestConfiguration.SCOPE + "2", key = "inner6")
    public void doSomething() {
        assert ScopedMethodsHolder.getCurrent(AspectTestConfiguration.SCOPE).equals("inner");
        assert ScopedMethodsHolder.getCurrent(AspectTestConfiguration.SCOPE + "2").equals("inner6");
    }
}
