package io.github.kshashov.scopedmethods.integration.proxy.impl;

public interface IService {
    @OuterScope
    void doSomething();
}
