package com.github.kshashov.scopedmethods;

import com.github.kshashov.scopedmethods.api.ScopedMethodsConfiguration;
import lombok.Setter;
import org.junit.jupiter.api.Test;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

public class ScopedMethodsManagerTest {

    @Test
    void getCurrent_Empty_ReturnsNull() {
        ScopedMethodsManager scopesManager = new ScopedMethodsManager(new ArrayList<>());

        assertNull(scopesManager.getCurrent());
        assertNull(scopesManager.getCurrent(""));
    }

    @Test
    void getCurrent_ReturnsCurrentScope() {
        ScopedMethodsManager scopesManager = new ScopedMethodsManager(new ArrayList<>());

        scopesManager.startScope("group1", "key1");
        assertEquals("key1", scopesManager.getCurrent("group1"));
        assertNull(scopesManager.getCurrent("group2"));

        scopesManager.startScope("group1", "key2");
        assertEquals("key2", scopesManager.getCurrent("group1"));
        assertNull(scopesManager.getCurrent("group2"));

        scopesManager.startScope("group2", "key3");
        assertEquals("key2", scopesManager.getCurrent("group1"));
        assertEquals("key3", scopesManager.getCurrent("group2"));

        scopesManager.popScope("group1");
        assertEquals("key1", scopesManager.getCurrent("group1"));
        assertEquals("key3", scopesManager.getCurrent("group2"));

        scopesManager.popScope("group2");
        assertEquals("key1", scopesManager.getCurrent("group1"));
        assertNull(scopesManager.getCurrent("group2"));

        scopesManager.popScope("group1");
        assertNull(scopesManager.getCurrent("group1"));
        assertNull(scopesManager.getCurrent("group2"));
    }

    @Test
    void getCurrent_EmptyGroup_ReturnsScopeForEmptyGroup() {
        ScopedMethodsManager scopesManager = new ScopedMethodsManager(new ArrayList<>());

        scopesManager.startScope("", "key1");
        assertEquals("key1", scopesManager.getCurrent());

        scopesManager.startScope("", "key2");
        assertEquals("key2", scopesManager.getCurrent());

        scopesManager.startScope("group", "key3");
        assertEquals("key2", scopesManager.getCurrent());

        scopesManager.popScope("");
        assertEquals("key1", scopesManager.getCurrent());

        scopesManager.popScope("");
        assertNull(scopesManager.getCurrent());
    }

    @Test
    void popScope_Empty_DoNothing() {
        ScopedMethodsManager scopesManager = new ScopedMethodsManager(new ArrayList<>());

        assertNull(scopesManager.getCurrent());

        assertDoesNotThrow(() -> {
            scopesManager.popScope("");
        });
        assertNull(scopesManager.getCurrent());

        scopesManager.startScope("", "key");
        scopesManager.popScope("");
        assertDoesNotThrow(() -> {
            scopesManager.popScope("");
        });

        assertNull(scopesManager.getCurrent());
    }

    @Test
    void startScope_WithConfiguration_ListenerInvoked() {
        TestScopedMethodsConfiguration groupConfig = new TestScopedMethodsConfiguration("group");
        TestScopedMethodsConfiguration emptyGroupConfig = new TestScopedMethodsConfiguration("");

        ArrayList<ScopedMethodsConfiguration> configs = new ArrayList<>();
        configs.add(groupConfig);
        configs.add(emptyGroupConfig);

        ScopedMethodsManager scopesManager = new ScopedMethodsManager(configs);

        emptyGroupConfig.setValidateScopelistener((s, s2) -> {
            assertEquals("key1", s);
            assertNull(s2);
            return s;
        });

        scopesManager.startScope("", "key1");
        assertEquals("key1", scopesManager.getCurrent());

        emptyGroupConfig.setValidateScopelistener((s, s2) -> {
            assertEquals("key2", s);
            assertEquals("key1", s2);
            return "key3";
        });

        scopesManager.startScope("", "key2");
        assertEquals("key3", scopesManager.getCurrent());
    }

    @Test
    void popScope_WithConfiguration_ListenerInvoked() {
        TestScopedMethodsConfiguration groupConfig = new TestScopedMethodsConfiguration("group");
        TestScopedMethodsConfiguration emptyGroupConfig = new TestScopedMethodsConfiguration("");

        ArrayList<ScopedMethodsConfiguration> configs = new ArrayList<>();
        configs.add(groupConfig);
        configs.add(emptyGroupConfig);

        emptyGroupConfig.setValidateScopelistener((s, s2) -> s);

        ScopedMethodsManager scopesManager = new ScopedMethodsManager(configs);

        scopesManager.startScope("", "key1");
        assertEquals("key1", scopesManager.getCurrent());

        scopesManager.startScope("", "key2");
        assertEquals("key2", scopesManager.getCurrent());

        emptyGroupConfig.setScopeFinishedListener(s -> {
            assertEquals("key2", s);
        });

        scopesManager.popScope("");
        assertEquals("key1", scopesManager.getCurrent());

        emptyGroupConfig.setScopeFinishedListener(s -> {
            assertEquals("key1", s);
        });

        scopesManager.popScope("");
        assertEquals(null, scopesManager.getCurrent());

        emptyGroupConfig.setScopeFinishedListener(s -> {
            assertNull(s);
        });
    }

    private static class TestScopedMethodsConfiguration implements ScopedMethodsConfiguration {
        private String group;
        @Setter
        private BiFunction<String, String, String> validateScopelistener;
        @Setter
        private Consumer<String> scopeFinishedListener;

        public TestScopedMethodsConfiguration(String group) {
            this.group = group;
            setValidateScopelistener((s, s2) -> {
                throw new IllegalStateException();
            });
            setScopeFinishedListener((s) -> {
                throw new IllegalStateException();
            });
        }

        @Override
        public @NotNull String getGroup() {
            return group;
        }

        @Override
        public @NotNull String validateScope(@NotNull String key, @NotNull String parentKey) {
            return validateScopelistener.apply(key, parentKey);
        }

        @Override
        public void onScopeFinished(@NotNull String key) {
            scopeFinishedListener.accept(key);
        }
    }
}
