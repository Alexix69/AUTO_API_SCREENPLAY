package com.foodtech.automation.api.screenplay.support.model;

import java.util.List;

public record OrderSnapshot(Long orderId, String tableNumber, String status, List<OrderProduct> products) {
}
