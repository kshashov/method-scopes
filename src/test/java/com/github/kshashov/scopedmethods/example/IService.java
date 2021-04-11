package com.github.kshashov.scopedmethods.example;

import com.github.kshashov.scopedmethods.api.ScopedMethod;

public interface IService {
    @ScopedMethod(group = MyScopeConfiguration.SCOPE, key = "outer")
    void doSomething();
}
