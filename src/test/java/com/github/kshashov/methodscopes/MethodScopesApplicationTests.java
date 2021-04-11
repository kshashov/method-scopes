package com.github.kshashov.methodscopes;

import com.github.kshashov.methodscopes.example.IService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MethodScopesApplicationTests extends BaseTest {

    @Autowired
    IService baseService;

    @Test
    void contextLoads() {
        baseService.doSomething();
    }

}
