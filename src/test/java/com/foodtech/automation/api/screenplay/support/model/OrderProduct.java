package com.foodtech.automation.api.screenplay.support.model;

import java.util.Map;

public record OrderProduct(String name, String type) {

    public Map<String, String> asMap() {
        return Map.of(
                "name", name,
                "type", type
        );
    }
}
