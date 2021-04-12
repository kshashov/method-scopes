package com.github.kshashov.scopedmethods;

import com.github.kshashov.scopedmethods.example.IService;
import com.github.kshashov.scopedmethods.example.MyScopeConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
class ScopedMethodsApplicationTests extends BaseTest {

    @Autowired
    IService baseService;
    @Autowired
    ScopedMethodsManager scopesManager;

    @Test
    void contextLoads() {
        assertNull(scopesManager.getCurrent(MyScopeConfiguration.SCOPE));
        baseService.doSomething();
        assertNull(scopesManager.getCurrent(MyScopeConfiguration.SCOPE));
    }

}
