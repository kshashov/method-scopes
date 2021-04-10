package com.github.kshashov.methodscopes.example;

import com.github.kshashov.methodscopes.MethodScopesManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ServiceImpl implements IService {
    @Autowired
    MethodScopesManager scopesManager;
    @Autowired
    Service2Impl service2;

    @Override
    public void doSomething() {
        log.info("current scope: " + scopesManager.getCurrent(MyScopeConfiguration.SCOPE));
        service2.doSomething();
        log.info("current scope: " + scopesManager.getCurrent(MyScopeConfiguration.SCOPE));
    }
}
