package com.github.kshashov.scopedmethods.example;

public interface IService {
    @OuterScope
    void doSomething();
}
