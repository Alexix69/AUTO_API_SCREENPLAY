package com.foodtech.automation.api.screenplay.support.api;

public final class ApiRoutes {

    public static final String LOGIN = "/api/auth/login";
    public static final String ORDERS = "/api/orders";
    public static final String TASKS_BY_BAR_STATION = "/api/tasks/station/BAR";

    private ApiRoutes() {
    }

    public static String orderById(Long orderId) {
        return ORDERS + "/" + orderId;
    }

    public static String taskStartPath(Long taskId) {
        return "/api/tasks/" + taskId + "/start";
    }
}
