package io.github.kshashov.scopedmethods.integration.proxy.impl;

import io.github.kshashov.scopedmethods.ScopedMethodsHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class Service2 {

    @Autowired
    Environment environment;

    @InnerScope
    public void doSomething() {
        if (Optional.ofNullable(environment.getProperty("scopedmethods.classAnnotationRequired")).orElse("").equals("true")) {
            assert ScopedMethodsHolder.getCurrent(MyScopeConfiguration.SCOPE).equals("outer");
        } else {
            assert ScopedMethodsHolder.getCurrent(MyScopeConfiguration.SCOPE).equals("inner");
        }
    }
}
