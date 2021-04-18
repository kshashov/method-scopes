package io.github.kshashov.scopedmethods.integration.proxy;

import io.github.kshashov.scopedmethods.ScopedMethodsHolder;
import io.github.kshashov.scopedmethods.integration.proxy.impl.IService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
class ProxyTest extends BaseIntegrationTest {

    @Autowired
    IService baseService;

    @Test
    void contextLoads() {
        assertNull(ScopedMethodsHolder.getCurrent(ProxyTestConfiguration.SCOPE));
        baseService.doSomething();
        assertNull(ScopedMethodsHolder.getCurrent(ProxyTestConfiguration.SCOPE));
    }
}
