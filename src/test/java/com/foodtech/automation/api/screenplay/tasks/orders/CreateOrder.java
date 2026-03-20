package com.foodtech.automation.api.screenplay.tasks.orders;

import com.foodtech.automation.api.screenplay.support.api.ApiRoutes;
import com.foodtech.automation.api.screenplay.support.context.CrudExecutionContext;
import com.foodtech.automation.api.screenplay.support.model.OrderPayload;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.rest.interactions.Post;

import java.util.List;
import java.util.Map;

public class CreateOrder implements Task {

    public static CreateOrder usingContextPayload() {
        return Tasks.instrumented(CreateOrder.class);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        CrudExecutionContext context = CrudExecutionContext.current();
        OrderPayload payload = context.createPayload();

        actor.attemptsTo(
                Post.to(ApiRoutes.ORDERS)
                        .with(request -> request
                                .contentType("application/json")
                                .header("Authorization", "Bearer " + context.token())
                                .body(payload.asMap())
                        )
        );

        context.setCreateStatus(SerenityRest.lastResponse().statusCode());

        Object rawOrderId = SerenityRest.lastResponse().jsonPath().get("orderId");
        Long orderId = rawOrderId == null ? null : Long.valueOf(String.valueOf(rawOrderId));
        if (orderId == null) {
            orderId = findOrderIdByTableNumber(context.token(), payload.tableNumber());
        }
        context.setOrderId(orderId);
    }

    private Long findOrderIdByTableNumber(String token, String tableNumber) {
        SerenityRest.given()
                .header("Authorization", "Bearer " + token)
                .get(ApiRoutes.TASKS_BY_BAR_STATION);

        List<Map<String, Object>> tasks = SerenityRest.lastResponse().jsonPath().getList("$");
        if (tasks == null) {
            throw new IllegalStateException("Could not infer orderId from tasks endpoint");
        }

        for (Map<String, Object> task : tasks) {
            Object table = task.get("tableNumber");
            if (tableNumber.equals(String.valueOf(table))) {
                Object orderId = task.get("orderId");
                if (orderId instanceof Number number) {
                    return number.longValue();
                }
            }
        }

        throw new IllegalStateException("Order ID was not found for tableNumber: " + tableNumber);
    }
}
