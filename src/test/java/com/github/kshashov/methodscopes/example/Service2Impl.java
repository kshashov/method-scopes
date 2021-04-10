package com.github.kshashov.methodscopes.example;

import com.github.kshashov.methodscopes.MethodScopesManager;
import com.github.kshashov.methodscopes.api.ScopedMethod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Service2Impl {
    @Autowired
    MethodScopesManager scopesManager;

    @ScopedMethod(group = MyScopeConfiguration.SCOPE, key = "inner")
    public void doSomething() {
        log.info("current scope: " + scopesManager.getCurrent(MyScopeConfiguration.SCOPE));
    }
}
