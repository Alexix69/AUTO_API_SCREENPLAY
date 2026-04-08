package com.foodtech.automation.api.screenplay.support.context;

import java.util.Objects;

public final class AuthorizationExecutionContext {

    private static final ThreadLocal<AuthorizationExecutionContext> CONTEXT = new ThreadLocal<>();

    private String token;
    private Long taskId;
    private String secondToken;
    private String orderId;

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

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long taskId() {
        return Objects.requireNonNull(taskId, "Missing context value: taskId");
    }

    public void setSecondToken(String secondToken) {
        this.secondToken = secondToken;
    }

    public String secondToken() {
        return Objects.requireNonNull(secondToken, "Missing context value: secondToken");
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String orderId() {
        return Objects.requireNonNull(orderId, "Missing context value: orderId");
    }
}
