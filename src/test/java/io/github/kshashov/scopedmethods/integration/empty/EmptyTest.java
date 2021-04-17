package io.github.kshashov.scopedmethods.integration.empty;

import io.github.kshashov.scopedmethods.ScopedMethodsHolder;
import io.github.kshashov.scopedmethods.integration.empty.impl.Service;
import io.github.kshashov.scopedmethods.integration.proxy.impl.MyScopeConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
class EmptyTest extends BaseIntegrationTest {

    @Autowired
    Service service;

    @Test
    void contextLoads() {
        assertNull(ScopedMethodsHolder.getCurrent(MyScopeConfiguration.SCOPE));
        service.doSomething();
        assertNull(ScopedMethodsHolder.getCurrent(MyScopeConfiguration.SCOPE));
    }
}
