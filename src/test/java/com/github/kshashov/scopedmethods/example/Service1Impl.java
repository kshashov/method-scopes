package com.github.kshashov.scopedmethods.example;

import com.github.kshashov.scopedmethods.ScopedMethodsHolder;
import com.github.kshashov.scopedmethods.api.EnableScopedMethods;
import com.github.kshashov.scopedmethods.example.inner.Service2Impl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableScopedMethods
public class Service1Impl implements IService {
    @Autowired
    Service2Impl service2;

    @Override
    public void doSomething() {
        assert ScopedMethodsHolder.getCurrent(MyScopeConfiguration.SCOPE).equals("outer");
        service2.doSomething();
        assert ScopedMethodsHolder.getCurrent(MyScopeConfiguration.SCOPE).equals("outer");
    }
}
