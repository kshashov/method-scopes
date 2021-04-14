package com.github.kshashov.scopedmethods;

import com.github.kshashov.scopedmethods.example.IService;
import com.github.kshashov.scopedmethods.example.MyScopeConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest(properties = "scopedmethods.packages:com.github.kshashov.scopedmethods.example")
class PackagesFilteringIntegrationTest extends BaseTest {

    @Autowired
    IService baseService;

    @Test
    void contextLoads() {
        assertNull(ScopedMethodsHolder.getCurrent(MyScopeConfiguration.SCOPE));
        baseService.doSomething();
        assertNull(ScopedMethodsHolder.getCurrent(MyScopeConfiguration.SCOPE));
    }
}
