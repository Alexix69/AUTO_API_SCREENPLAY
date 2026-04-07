package com.foodtech.automation.api.screenplay.stepdefinitions.task;

import com.foodtech.automation.api.screenplay.questions.authorization.ResponseStatusIs;
import com.foodtech.automation.api.screenplay.support.actors.ApiActors;
import com.foodtech.automation.api.screenplay.support.context.AuthorizationExecutionContext;
import com.foodtech.automation.api.screenplay.tasks.authorization.CallTaskStartEndpoint;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static org.hamcrest.Matchers.is;

public class TaskStartStepDefinitions {

    @Given("a COCINERO actor is authenticated with a valid token")
    public void aCocineroActorIsAuthenticatedWithAValidToken() {
    }

    @When("the actor calls PATCH on a BARTENDER task start endpoint")
    public void theActorCallsPatchOnBartenderTaskStartEndpoint() {
        Long taskId = AuthorizationExecutionContext.current().taskId();
        ApiActors.spotlight().attemptsTo(CallTaskStartEndpoint.forTask(taskId));
    }

    @When("the actor calls PATCH on a task already in preparation")
    public void theActorCallsPatchOnATaskAlreadyInPreparation() {
        Long taskId = AuthorizationExecutionContext.current().taskId();
        ApiActors.spotlight().attemptsTo(CallTaskStartEndpoint.forTask(taskId));
    }

    @Then("the API responds with HTTP 409")
    public void theApiRespondsWith409() {
        ApiActors.spotlight().should(seeThat(ResponseStatusIs.equalTo(409), is(true)));
    }
}

