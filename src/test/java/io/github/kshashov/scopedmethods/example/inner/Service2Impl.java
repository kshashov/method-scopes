package io.github.kshashov.scopedmethods.example.inner;

import io.github.kshashov.scopedmethods.ScopedMethodsHolder;
import io.github.kshashov.scopedmethods.ScopedMethodsProperties;
import io.github.kshashov.scopedmethods.example.InnerScope;
import io.github.kshashov.scopedmethods.example.MyScopeConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Service2Impl {
    @Autowired
    ScopedMethodsProperties properties;

    @InnerScope
    public void doSomething() {
        if (properties.isClassAnnotationRequired() || (properties.getPackages().length > 0)) {
            assert ScopedMethodsHolder.getCurrent(MyScopeConfiguration.SCOPE).equals("outer");
        } else {
            assert ScopedMethodsHolder.getCurrent(MyScopeConfiguration.SCOPE).equals("inner");
        }
    }
}
