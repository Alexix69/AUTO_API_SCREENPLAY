package com.foodtech.automation.api.screenplay.support.context;

import java.util.Objects;

public final class TaskCompletionExecutionContext {

    private static final ThreadLocal<TaskCompletionExecutionContext> CONTEXT = new ThreadLocal<>();

    private String orderId;
    private Long cocineroTaskId;
    private Long bartenderTaskId;
    private String cocineroToken;
    private String bartenderToken;

    private TaskCompletionExecutionContext() {
    }

    public static TaskCompletionExecutionContext init() {
        TaskCompletionExecutionContext context = new TaskCompletionExecutionContext();
        CONTEXT.set(context);
        return context;
    }

    public static TaskCompletionExecutionContext current() {
        TaskCompletionExecutionContext context = CONTEXT.get();
        if (context == null) {
            throw new IllegalStateException("TaskCompletionExecutionContext is not initialized");
        }
        return context;
    }

    public static void clear() {
        CONTEXT.remove();
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String orderId() {
        return Objects.requireNonNull(orderId, "Missing context value: orderId");
    }

    public void setCocineroTaskId(Long cocineroTaskId) {
        this.cocineroTaskId = cocineroTaskId;
    }

    public Long cocineroTaskId() {
        return Objects.requireNonNull(cocineroTaskId, "Missing context value: cocineroTaskId");
    }

    public void setBartenderTaskId(Long bartenderTaskId) {
        this.bartenderTaskId = bartenderTaskId;
    }

    public Long bartenderTaskId() {
        return Objects.requireNonNull(bartenderTaskId, "Missing context value: bartenderTaskId");
    }

    public void setCocineroToken(String cocineroToken) {
        this.cocineroToken = cocineroToken;
    }

    public String cocineroToken() {
        return Objects.requireNonNull(cocineroToken, "Missing context value: cocineroToken");
    }

    public void setBartenderToken(String bartenderToken) {
        this.bartenderToken = bartenderToken;
    }

    public String bartenderToken() {
        return Objects.requireNonNull(bartenderToken, "Missing context value: bartenderToken");
    }
}
