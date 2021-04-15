package io.github.kshashov.scopedmethods;

import io.github.kshashov.scopedmethods.example.IService;
import io.github.kshashov.scopedmethods.example.MyScopeConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest(properties = "scopedmethods.classAnnotationRequired:true")
class EnableScopedMethodsIntegrationTest extends BaseIntegrationTest {

    @Autowired
    IService baseService;

    @Test
    void contextLoads() {
        assertNull(ScopedMethodsHolder.getCurrent(MyScopeConfiguration.SCOPE));
        baseService.doSomething();
        assertNull(ScopedMethodsHolder.getCurrent(MyScopeConfiguration.SCOPE));
    }
}
