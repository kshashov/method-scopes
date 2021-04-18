package io.github.kshashov.scopedmethods.integration.empty;

import io.github.kshashov.scopedmethods.ScopedMethodsHolder;
import io.github.kshashov.scopedmethods.integration.empty.impl.Service;
import io.github.kshashov.scopedmethods.integration.proxy.ProxyTestConfiguration;
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
        // Not working without Enable* annotation
        assertNull(ScopedMethodsHolder.getCurrent(ProxyTestConfiguration.SCOPE));
        service.doSomething();
        assertNull(ScopedMethodsHolder.getCurrent(ProxyTestConfiguration.SCOPE));
    }
}
