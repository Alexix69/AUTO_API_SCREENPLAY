package com.foodtech.automation.api.screenplay.support.config;

public final class TestEnvironment {

    private static final String DEFAULT_BASE_URL = "http://localhost:8080";

    private TestEnvironment() {
    }

    public static String apiBaseUrl() {
        return read("FOODTECH_API_BASE_URL", DEFAULT_BASE_URL);
    }

    private static String read(String key, String fallback) {
        String value = System.getenv(key);
        return value == null || value.isBlank() ? fallback : value;
    }
}
