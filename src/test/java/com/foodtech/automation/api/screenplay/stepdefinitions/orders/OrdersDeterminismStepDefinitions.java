package com.foodtech.automation.api.screenplay.stepdefinitions.orders;

import com.foodtech.automation.api.screenplay.support.actors.ApiActors;
import com.foodtech.automation.api.screenplay.support.context.CrudExecutionContext;
import com.foodtech.automation.api.screenplay.support.data.OrderTestDataFactory;
import com.foodtech.automation.api.screenplay.support.model.OrderPayload;
import com.foodtech.automation.api.screenplay.tasks.orders.CreateOrder;
import com.foodtech.automation.api.screenplay.tasks.orders.DeleteOrderById;
import com.foodtech.automation.api.screenplay.tasks.orders.GetOrderById;
import com.foodtech.automation.api.screenplay.tasks.orders.UpdateOrderById;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.screenplay.Actor;

import static org.junit.Assert.assertEquals;

public class OrdersDeterminismStepDefinitions {

    @When("the actor executes the orders lifecycle twice with isolated data")
    public void theActorExecutesTheOrdersLifecycleTwiceWithIsolatedData() {
        Actor actor = ApiActors.spotlight();
        String token = CrudExecutionContext.current().token();

        executeLifecycleIteration(actor);

        OrderPayload createPayload = OrderTestDataFactory.createPayload();
        OrderPayload updatePayload = OrderTestDataFactory.updatePayloadFrom(createPayload);
        CrudExecutionContext.init(createPayload, updatePayload).setToken(token);

        executeLifecycleIteration(actor);
    }

    @Then("both executions complete without data collisions")
    public void bothExecutionsCompleteWithoutDataCollisions() {
        OrdersCrudAssertions.shouldHaveStatus(ApiActors.spotlight(), 404);
    }

    private void executeLifecycleIteration(Actor actor) {
        actor.attemptsTo(CreateOrder.usingContextPayload());
        assertEquals(201, CrudExecutionContext.current().createStatus().intValue());
        CrudExecutionContext.current().orderId();

        actor.attemptsTo(GetOrderById.fromContext());
        OrdersCrudAssertions.shouldHaveStatus(actor, 200);

        actor.attemptsTo(UpdateOrderById.usingContextPayload());
        OrdersCrudAssertions.shouldHaveStatus(actor, 200);

        actor.attemptsTo(DeleteOrderById.fromContext());
        OrdersCrudAssertions.shouldHaveStatus(actor, 204);

        actor.attemptsTo(GetOrderById.fromContext());
        OrdersCrudAssertions.shouldHaveStatus(actor, 404);
    }
}
