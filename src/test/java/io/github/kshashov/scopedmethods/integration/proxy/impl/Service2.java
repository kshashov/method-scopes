package io.github.kshashov.scopedmethods.integration.proxy.impl;

import io.github.kshashov.scopedmethods.ScopedMethodsHolder;
import io.github.kshashov.scopedmethods.integration.proxy.ProxyTestConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class Service2 {

    @Autowired
    Environment environment;

    @InnerScope
    public void doSomething() {
        assert ScopedMethodsHolder.getCurrent(ProxyTestConfiguration.SCOPE).equals("inner");
    }
}
