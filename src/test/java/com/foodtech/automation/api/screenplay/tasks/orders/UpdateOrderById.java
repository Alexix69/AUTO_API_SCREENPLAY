package com.foodtech.automation.api.screenplay.tasks.orders;

import com.foodtech.automation.api.screenplay.support.api.ApiRoutes;
import com.foodtech.automation.api.screenplay.support.context.CrudExecutionContext;
import com.foodtech.automation.api.screenplay.support.model.OrderPayload;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.rest.interactions.Put;

import java.util.Map;

public class UpdateOrderById implements Task {

    public static UpdateOrderById usingContextPayload() {
        return Tasks.instrumented(UpdateOrderById.class);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        CrudExecutionContext context = CrudExecutionContext.current();
        OrderPayload payload = context.updatePayload();

        actor.attemptsTo(
                Put.to(ApiRoutes.orderById(context.orderId()))
                        .with(request -> request
                                .contentType("application/json")
                                .header("Authorization", "Bearer " + context.token())
                                .body(Map.of("tableNumber", payload.tableNumber()))
                        )
        );
    }
}
