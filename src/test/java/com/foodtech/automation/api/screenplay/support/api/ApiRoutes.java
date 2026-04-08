package com.foodtech.automation.api.screenplay.support.api;

public final class ApiRoutes {

    public static final String LOGIN = "/api/auth/login";
    public static final String REGISTER = "/api/auth/register";
    public static final String ORDERS = "/api/orders";
    public static final String TASKS_BY_BAR_STATION = "/api/tasks/station/BAR";
    public static final String TASKS_BY_HOT_KITCHEN = "/api/tasks/station/HOT_KITCHEN";

    private ApiRoutes() {
    }

    public static String orderById(Long orderId) {
        return ORDERS + "/" + orderId;
    }

    public static String taskStartPath(Long taskId) {
        return "/api/tasks/" + taskId + "/start";
    }

    public static String taskCompletePath(Long taskId) {
        return "/api/tasks/" + taskId + "/complete";
    }

    public static String orderStatusPath(String orderId) {
        return "/api/orders/" + orderId + "/status";
    }
}
