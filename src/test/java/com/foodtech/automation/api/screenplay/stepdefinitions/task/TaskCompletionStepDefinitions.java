package com.foodtech.automation.api.screenplay.stepdefinitions.task;

import com.foodtech.automation.api.screenplay.questions.LastResponseBodyField;
import com.foodtech.automation.api.screenplay.questions.authorization.ConcurrentStartProducedExactlyOneSuccess;
import com.foodtech.automation.api.screenplay.questions.authorization.OrderStatusIs;
import com.foodtech.automation.api.screenplay.support.actors.ApiActors;
import com.foodtech.automation.api.screenplay.support.context.AuthorizationExecutionContext;
import com.foodtech.automation.api.screenplay.tasks.authorization.CallOrderStatusEndpoint;
import com.foodtech.automation.api.screenplay.tasks.authorization.CallTaskCompleteEndpoint;
import com.foodtech.automation.api.screenplay.tasks.authorization.ExecuteConcurrentTaskStart;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class TaskCompletionStepDefinitions {

    @Given("an order contains an IN_PREPARATION task and at least one other non-completed task")
    public void anOrderContainsInPrepTaskAndOtherPendingTask() {
    }

    @Given("an order contains exactly one IN_PREPARATION task and all other tasks are already COMPLETED")
    public void anOrderContainsOnlyOneInPrepTaskAndAllOthersCompleted() {
    }

    @Given("a task is in PENDING state and the complete call is made on it")
    public void aTaskIsInPendingState() {
    }

    @Given("two COCINERO operators are authenticated and a PENDING task exists")
    public void twoCocineroOperatorsAuthenticatedAndPendingTaskExists() {
    }

    @When("the complete-task endpoint is called for the IN_PREPARATION task")
    public void theCompleteTaskEndpointIsCalledForInPrepTask() {
        Long taskId = AuthorizationExecutionContext.current().taskId();
        ApiActors.spotlight().attemptsTo(CallTaskCompleteEndpoint.forTask(taskId));
    }

    @When("the complete-task endpoint is called for the PENDING task")
    public void theCompleteTaskEndpointIsCalledForPendingTask() {
        Long taskId = AuthorizationExecutionContext.current().taskId();
        ApiActors.spotlight().attemptsTo(CallTaskCompleteEndpoint.forTask(taskId));
    }

    @When("both operators simultaneously call the start-preparation endpoint for that task")
    public void bothOperatorsSimultaneouslyCallStartPreparation() {
        ApiActors.spotlight().attemptsTo(ExecuteConcurrentTaskStart.onSameTask());
    }

    @Then("the task status is COMPLETED")
    public void theTaskStatusIsCompleted() {
        ApiActors.spotlight().should(seeThat(LastResponseBodyField.valueOf("status"), equalTo("COMPLETED")));
    }

    @Then("the order status is not COMPLETED")
    public void theOrderStatusIsNotCompleted() {
        String orderId = AuthorizationExecutionContext.current().orderId();
        ApiActors.spotlight().attemptsTo(CallOrderStatusEndpoint.forOrder(orderId));
        ApiActors.spotlight().should(seeThat(OrderStatusIs.equalTo("COMPLETED"), is(false)));
    }

    @Then("the order status is COMPLETED")
    public void theOrderStatusIsCompleted() {
        String orderId = AuthorizationExecutionContext.current().orderId();
        ApiActors.spotlight().attemptsTo(CallOrderStatusEndpoint.forOrder(orderId));
        ApiActors.spotlight().should(seeThat(OrderStatusIs.equalTo("COMPLETED"), is(true)));
    }

    @Then("exactly one response is HTTP 200 and the other is HTTP 409")
    public void exactlyOneResponseIs200AndOtherIs409() {
        ApiActors.spotlight().should(seeThat(
                ConcurrentStartProducedExactlyOneSuccess.andOneConflict(), is(true)));
    }
}
