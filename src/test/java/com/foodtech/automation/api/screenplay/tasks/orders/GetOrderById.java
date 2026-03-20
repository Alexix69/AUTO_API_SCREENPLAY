package com.foodtech.automation.api.screenplay.tasks.orders;

import com.foodtech.automation.api.screenplay.support.api.ApiRoutes;
import com.foodtech.automation.api.screenplay.support.context.CrudExecutionContext;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.rest.interactions.Get;

public class GetOrderById implements Task {

    public static GetOrderById fromContext() {
        return Tasks.instrumented(GetOrderById.class);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        CrudExecutionContext context = CrudExecutionContext.current();
        actor.attemptsTo(
                Get.resource(ApiRoutes.orderById(context.orderId()))
                        .with(request -> request
                                .header("Authorization", "Bearer " + context.token())
                        )
        );
    }
}
