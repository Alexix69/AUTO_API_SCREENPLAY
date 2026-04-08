package com.foodtech.automation.api.screenplay.stepdefinitions.authorization;

import com.foodtech.automation.api.screenplay.questions.authorization.ResponseStatusIs;
import com.foodtech.automation.api.screenplay.support.actors.ApiActors;
import com.foodtech.automation.api.screenplay.tasks.authorization.CallUnauthorizedEndpoint;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static org.hamcrest.Matchers.is;

public class AuthorizationStepDefinitions {

    @Given("a role-restricted actor is authenticated and ready")
    public void aRoleRestrictedActorIsAuthenticatedAndReady() {
    }

    @Given("the api actor attempts to register with an invalid role")
    public void theApiActorAttemptsToRegisterWithAnInvalidRole() {
    }

    @When("the registration request is submitted")
    public void theRegistrationRequestIsSubmitted() {
    }

    @When("the actor calls POST {string}")
    public void theActorCallsPost(String path) {
        ApiActors.spotlight().attemptsTo(CallUnauthorizedEndpoint.post(path));
    }

    @When("the actor calls GET {string}")
    public void theActorCallsGet(String path) {
        ApiActors.spotlight().attemptsTo(CallUnauthorizedEndpoint.get(path));
    }

    @Then("the API responds with HTTP 403")
    public void theApiRespondsWith403() {
        ApiActors.spotlight().should(seeThat(ResponseStatusIs.equalTo(403), is(true)));
    }

    @Then("the API responds with the expected validation error status")
    public void theApiRespondsWithExpectedValidationErrorStatus() {
        ApiActors.spotlight().should(seeThat(ResponseStatusIs.equalTo(400), is(true)));
    }
}
