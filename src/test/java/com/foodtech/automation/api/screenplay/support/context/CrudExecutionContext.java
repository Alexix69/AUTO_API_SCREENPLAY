package com.foodtech.automation.api.screenplay.support.context;

import com.foodtech.automation.api.screenplay.support.model.OrderPayload;

import java.util.Objects;

public final class CrudExecutionContext {

    private static final ThreadLocal<CrudExecutionContext> CONTEXT = new ThreadLocal<>();

    private String token;
    private Long orderId;
    private Integer createStatus;
    private OrderPayload createPayload;
    private OrderPayload updatePayload;

    private CrudExecutionContext() {
    }

    public static CrudExecutionContext init(OrderPayload createPayload, OrderPayload updatePayload) {
        CrudExecutionContext context = new CrudExecutionContext();
        context.createPayload = createPayload;
        context.updatePayload = updatePayload;
        CONTEXT.set(context);
        return context;
    }

    public static CrudExecutionContext current() {
        CrudExecutionContext context = CONTEXT.get();
        if (context == null) {
            throw new IllegalStateException("CrudExecutionContext is not initialized");
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
        return require(token, "token");
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long orderId() {
        return require(orderId, "orderId");
    }

    public void setCreateStatus(Integer createStatus) {
        this.createStatus = createStatus;
    }

    public Integer createStatus() {
        return require(createStatus, "createStatus");
    }

    public OrderPayload createPayload() {
        return require(createPayload, "createPayload");
    }

    public OrderPayload updatePayload() {
        return require(updatePayload, "updatePayload");
    }

    private <T> T require(T value, String field) {
        return Objects.requireNonNull(value, "Missing context value: " + field);
    }
}
