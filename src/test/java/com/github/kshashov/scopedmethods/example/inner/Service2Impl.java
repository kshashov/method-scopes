package com.github.kshashov.scopedmethods.example.inner;

import com.github.kshashov.scopedmethods.ScopedMethodsHolder;
import com.github.kshashov.scopedmethods.ScopedMethodsProperties;
import com.github.kshashov.scopedmethods.example.InnerScope;
import com.github.kshashov.scopedmethods.example.MyScopeConfiguration;
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
