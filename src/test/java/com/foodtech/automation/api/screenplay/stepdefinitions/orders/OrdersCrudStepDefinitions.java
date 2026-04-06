package com.foodtech.automation.api.screenplay.stepdefinitions.orders;

import com.foodtech.automation.api.screenplay.support.actors.ApiActors;
import com.foodtech.automation.api.screenplay.support.context.CrudExecutionContext;
import com.foodtech.automation.api.screenplay.tasks.auth.AuthenticateUser;
import com.foodtech.automation.api.screenplay.tasks.orders.CreateOrder;
import com.foodtech.automation.api.screenplay.tasks.orders.DeleteOrderById;
import com.foodtech.automation.api.screenplay.tasks.orders.GetOrderById;
import com.foodtech.automation.api.screenplay.tasks.orders.UpdateOrderById;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.screenplay.Actor;

import static org.junit.Assert.assertEquals;

public class OrdersCrudStepDefinitions {

    @Given("the api actor is authenticated")
    public void theApiActorIsAuthenticated() {
        ApiActors.spotlight().attemptsTo(AuthenticateUser.withDefaultCredentials());
    }

    @When("the actor creates a new order")
    public void theActorCreatesANewOrder() {
        Actor actor = ApiActors.spotlight();
        actor.attemptsTo(CreateOrder.usingContextPayload());
    }

    @Then("the order is created with a reusable order id")
    public void theOrderIsCreatedWithAReusableOrderId() {
        Actor actor = ApiActors.spotlight();
        assertEquals(201, CrudExecutionContext.current().createStatus().intValue());
        CrudExecutionContext.current().orderId();
    }

    @When("the actor retrieves the same order")
    public void theActorRetrievesTheSameOrder() {
        ApiActors.spotlight().attemptsTo(GetOrderById.fromContext());
    }

    @Then("the retrieved order matches the created data")
    public void theRetrievedOrderMatchesTheCreatedData() {
        Actor actor = ApiActors.spotlight();
        OrdersCrudAssertions.shouldHaveStatus(actor, 200);
        OrdersCrudAssertions.shouldHaveFieldValue(actor, "orderId", String.valueOf(CrudExecutionContext.current().orderId()));
        OrdersCrudAssertions.shouldHaveFieldValue(actor, "tableNumber", CrudExecutionContext.current().createPayload().tableNumber());
    }

    @When("the actor updates the order table number")
    public void theActorUpdatesTheOrderTableNumber() {
        ApiActors.spotlight().attemptsTo(UpdateOrderById.usingContextPayload());
    }

    @Then("the order reflects the updated table number")
    public void theOrderReflectsTheUpdatedTableNumber() {
        Actor actor = ApiActors.spotlight();
        OrdersCrudAssertions.shouldHaveStatus(actor, 200);
        OrdersCrudAssertions.shouldHaveFieldValue(actor, "orderId", String.valueOf(CrudExecutionContext.current().orderId()));
        OrdersCrudAssertions.shouldHaveFieldValue(actor, "tableNumber", CrudExecutionContext.current().updatePayload().tableNumber());
    }

    @When("the actor deletes the order")
    public void theActorDeletesTheOrder() {
        ApiActors.spotlight().attemptsTo(DeleteOrderById.fromContext());
    }

    @Then("the delete operation is acknowledged")
    public void theDeleteOperationIsAcknowledged() {
        OrdersCrudAssertions.shouldHaveStatus(ApiActors.spotlight(), 204);
    }

    @And("the actor requests the deleted order again")
    public void theActorRequestsTheDeletedOrderAgain() {
        ApiActors.spotlight().attemptsTo(GetOrderById.fromContext());
    }

    @Then("the service reports order not found")
    public void theServiceReportsOrderNotFound() {
        OrdersCrudAssertions.shouldHaveStatus(ApiActors.spotlight(), 404);
    }
}
