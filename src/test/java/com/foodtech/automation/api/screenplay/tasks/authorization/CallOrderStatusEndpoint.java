package com.foodtech.automation.api.screenplay.tasks.authorization;

import com.foodtech.automation.api.screenplay.support.api.ApiRoutes;
import com.foodtech.automation.api.screenplay.support.context.AuthorizationExecutionContext;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Get;

public class CallOrderStatusEndpoint implements Task {

    private final String orderId;

    private CallOrderStatusEndpoint(String orderId) {
        this.orderId = orderId;
    }

    public static CallOrderStatusEndpoint forOrder(String orderId) {
        return new CallOrderStatusEndpoint(orderId);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        String token = AuthorizationExecutionContext.current().token();
        actor.attemptsTo(
                Get.resource(ApiRoutes.orderStatusPath(orderId))
                        .with(request -> request
                                .header("Authorization", "Bearer " + token))
        );
    }
}
