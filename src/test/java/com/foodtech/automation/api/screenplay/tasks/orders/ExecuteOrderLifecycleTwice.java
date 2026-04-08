package com.foodtech.automation.api.screenplay.tasks.orders;

import com.foodtech.automation.api.screenplay.support.context.CrudExecutionContext;
import com.foodtech.automation.api.screenplay.support.data.OrderTestDataFactory;
import com.foodtech.automation.api.screenplay.support.model.OrderPayload;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;

public class ExecuteOrderLifecycleTwice implements Task {

    public static ExecuteOrderLifecycleTwice forTheCurrentOrder() {
        return Tasks.instrumented(ExecuteOrderLifecycleTwice.class);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        String token = CrudExecutionContext.current().token();

        executeLifecycleIteration(actor);

        OrderPayload createPayload = OrderTestDataFactory.createPayload();
        OrderPayload updatePayload = OrderTestDataFactory.updatePayloadFrom(createPayload);
        CrudExecutionContext.init(createPayload, updatePayload).setToken(token);

        executeLifecycleIteration(actor);
    }

    private void executeLifecycleIteration(Actor actor) {
        actor.attemptsTo(CreateOrder.usingContextPayload());

        actor.attemptsTo(GetOrderById.fromContext());

        actor.attemptsTo(UpdateOrderById.usingContextPayload());

        actor.attemptsTo(DeleteOrderById.fromContext());

        actor.attemptsTo(GetOrderById.fromContext());
    }
}
