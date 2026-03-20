package com.foodtech.automation.api.screenplay.support.config;

public final class TestEnvironment {

    private static final String DEFAULT_BASE_URL = "http://localhost:8080";
    private static final String DEFAULT_USER = "admin@foodtech.com";
    private static final String DEFAULT_PASSWORD = "Admin123";

    private TestEnvironment() {
    }

    public static String apiBaseUrl() {
        return read("FOODTECH_API_BASE_URL", DEFAULT_BASE_URL);
    }

    public static String automationUser() {
        return read("FOODTECH_AUTOMATION_USER", DEFAULT_USER);
    }

    public static String automationPassword() {
        return read("FOODTECH_AUTOMATION_PASSWORD", DEFAULT_PASSWORD);
    }

    private static String read(String key, String fallback) {
        String value = System.getenv(key);
        return value == null || value.isBlank() ? fallback : value;
    }
}
