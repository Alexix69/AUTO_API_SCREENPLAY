package com.foodtech.automation.api.screenplay.support.context;

import java.util.Objects;

public final class AuthorizationExecutionContext {

    private static final ThreadLocal<AuthorizationExecutionContext> CONTEXT = new ThreadLocal<>();

    private String token;

    private AuthorizationExecutionContext() {
    }

    public static AuthorizationExecutionContext init() {
        AuthorizationExecutionContext context = new AuthorizationExecutionContext();
        CONTEXT.set(context);
        return context;
    }

    public static AuthorizationExecutionContext current() {
        AuthorizationExecutionContext context = CONTEXT.get();
        if (context == null) {
            throw new IllegalStateException("AuthorizationExecutionContext is not initialized");
        }
        return context;
    }

    public static void clear() {
        CONTEXT.remove();
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String token() {
        return Objects.requireNonNull(token, "Missing context value: token");
    }
}
