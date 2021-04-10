package com.github.kshashov.methodscopes.example;

import com.github.kshashov.methodscopes.api.ScopedMethod;

public interface IService {
    @ScopedMethod(group = MyScopeConfiguration.SCOPE, key = "outer")
    void doSomething();
}
