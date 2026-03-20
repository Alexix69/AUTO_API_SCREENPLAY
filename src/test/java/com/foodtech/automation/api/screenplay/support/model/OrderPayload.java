package com.foodtech.automation.api.screenplay.support.model;

import java.util.List;
import java.util.Map;

public record OrderPayload(String tableNumber, List<OrderProduct> products) {

    public Map<String, Object> asMap() {
        return Map.of(
                "tableNumber", tableNumber,
                "products", products.stream().map(OrderProduct::asMap).toList()
        );
    }

    public OrderPayload withTableNumber(String newTableNumber) {
        return new OrderPayload(newTableNumber, products);
    }
}
