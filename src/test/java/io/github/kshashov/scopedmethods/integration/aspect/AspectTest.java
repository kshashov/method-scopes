package io.github.kshashov.scopedmethods.integration.aspect;

import io.github.kshashov.scopedmethods.ScopedMethodsHolder;
import io.github.kshashov.scopedmethods.integration.aspect.impl.Service1;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.assertNull;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@SpringBootTest
class AspectTest extends BaseIntegrationTest {

    @Autowired
    Service1 baseService;

    @Test
    void contextLoads() {
        assertNull(ScopedMethodsHolder.getCurrent(AspectTestConfiguration.SCOPE));
        baseService.doSomething();
        baseService.doSomething2();
        baseService.doSomething3();
        assertNull(ScopedMethodsHolder.getCurrent(AspectTestConfiguration.SCOPE));
    }
}
