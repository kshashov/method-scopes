package com.github.kshashov.scopedmethods;

import com.github.kshashov.scopedmethods.example.IService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ScopedMethodsApplicationTests extends BaseTest {

    @Autowired
    IService baseService;

    @Test
    void contextLoads() {
        baseService.doSomething();
    }

}
