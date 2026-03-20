package com.foodtech.automation.api.screenplay.support.data;

import com.foodtech.automation.api.screenplay.support.model.OrderPayload;
import com.foodtech.automation.api.screenplay.support.model.OrderProduct;

import java.time.Instant;
import java.util.List;

public final class OrderTestDataFactory {

    private OrderTestDataFactory() {
    }

    public static OrderPayload createPayload() {
        String suffix = String.valueOf(Instant.now().toEpochMilli());
        return new OrderPayload(
                "AUTO-TBL-" + suffix,
                List.of(
                    new OrderProduct("Soup-" + suffix, "HOT_DISH"),
                    new OrderProduct("Juice-" + suffix, "DRINK")
                )
        );
    }

    public static OrderPayload updatePayloadFrom(OrderPayload original) {
        String suffix = String.valueOf(Instant.now().toEpochMilli());
        return original.withTableNumber("AUTO-TBL-UPD-" + suffix);
    }
}
