package io.github.kshashov.scopedmethods.integration.proxy.impl;

import io.github.kshashov.scopedmethods.ScopedMethodsHolder;
import io.github.kshashov.scopedmethods.integration.proxy.ProxyTestConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Service1 implements IService {

    @Autowired
    Service2 service2;

    @Override
    public void doSomething() {
        assert ScopedMethodsHolder.getCurrent(ProxyTestConfiguration.SCOPE).equals("outer");
        service2.doSomething();
        assert ScopedMethodsHolder.getCurrent(ProxyTestConfiguration.SCOPE).equals("outer");
    }
}
