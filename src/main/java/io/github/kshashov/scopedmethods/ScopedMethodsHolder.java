package io.github.kshashov.scopedmethods;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class ScopedMethodsHolder {
    static final ThreadLocal<Map<String, Stack<String>>> ACTIVE_SCOPES = ThreadLocal.withInitial(HashMap::new);

    /**
     * Shortcut for {@link #getCurrent(String)} for empty group.
     *
     * @return the current scope
     */
    public static String getCurrent() {
        return getCurrent("");
    }

    /**
     * Returns the current scope id for the specified group.
     *
     * @param group group id
     * @return the current scope id for the specified group or {@code null} if nothing
     */
    public static String getCurrent(@NotNull String group) {
        Map<String, Stack<String>> scopes = ACTIVE_SCOPES.get();

        Stack<String> groupScopes = scopes.get(group);
        if ((groupScopes == null) || groupScopes.empty()) {
            return null;
        }

        return groupScopes.peek();
    }
}
