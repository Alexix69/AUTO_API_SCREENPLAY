package com.foodtech.automation.api.screenplay.tasks.orders;

import com.foodtech.automation.api.screenplay.support.api.ApiRoutes;
import com.foodtech.automation.api.screenplay.support.context.CrudExecutionContext;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.rest.interactions.Delete;

public class DeleteOrderById implements Task {

    public static DeleteOrderById fromContext() {
        return Tasks.instrumented(DeleteOrderById.class);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        CrudExecutionContext context = CrudExecutionContext.current();

        actor.attemptsTo(
                Delete.from(ApiRoutes.orderById(context.orderId()))
                        .with(request -> request
                                .header("Authorization", "Bearer " + context.token())
                        )
        );
    }
}
