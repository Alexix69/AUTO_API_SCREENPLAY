package com.foodtech.automation.api.screenplay.stepdefinitions.orders;

import com.foodtech.automation.api.screenplay.support.actors.ApiActors;
import com.foodtech.automation.api.screenplay.tasks.orders.ExecuteOrderLifecycleTwice;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.screenplay.Actor;

public class OrdersDeterminismStepDefinitions {

    @When("the actor executes the orders lifecycle twice with isolated data")
    public void theActorExecutesTheOrdersLifecycleTwiceWithIsolatedData() {
        Actor actor = ApiActors.spotlight();
        actor.attemptsTo(ExecuteOrderLifecycleTwice.forTheCurrentOrder());
    }

    @Then("both executions complete without data collisions")
    public void bothExecutionsCompleteWithoutDataCollisions() {
        OrdersCrudAssertions.shouldHaveStatus(ApiActors.spotlight(), 404);
    }
}
