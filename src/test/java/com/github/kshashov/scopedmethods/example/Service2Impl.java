package com.github.kshashov.scopedmethods.example;

import com.github.kshashov.scopedmethods.ScopedMethodsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Service2Impl {
    @Autowired
    ScopedMethodsManager scopesManager;

    @InnerScope
    public void doSomething() {
        assert scopesManager.getCurrent(MyScopeConfiguration.SCOPE).equals("inner");
    }
}
