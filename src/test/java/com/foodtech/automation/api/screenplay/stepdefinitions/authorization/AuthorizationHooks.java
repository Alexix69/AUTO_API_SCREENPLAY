package com.foodtech.automation.api.screenplay.stepdefinitions.authorization;

import com.foodtech.automation.api.screenplay.support.actors.ApiActors;
import com.foodtech.automation.api.screenplay.support.context.AuthorizationExecutionContext;
import com.foodtech.automation.api.screenplay.tasks.authorization.AuthenticateUserWithRole;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import net.serenitybdd.screenplay.Actor;

public class AuthorizationHooks {

    @Before("@apiAuthCocineroOrders")
    public void beforeCocineroOrders() {
        Actor actor = ApiActors.openStage();
        AuthorizationExecutionContext.init();
        actor.attemptsTo(AuthenticateUserWithRole.as("COCINERO"));
    }

    @Before("@apiAuthMeseroBarTasks")
    public void beforeMeseroBarTasks() {
        Actor actor = ApiActors.openStage();
        AuthorizationExecutionContext.init();
        actor.attemptsTo(AuthenticateUserWithRole.as("MESERO"));
    }

    @Before("@apiAuthMeseroTasks")
    public void beforeMeseroTasks() {
        Actor actor = ApiActors.openStage();
        AuthorizationExecutionContext.init();
        actor.attemptsTo(AuthenticateUserWithRole.as("MESERO"));
    }

    @After("@apiAuthCocineroOrders or @apiAuthMeseroBarTasks or @apiAuthMeseroTasks")
    public void afterAuthorizationScenario() {
        AuthorizationExecutionContext.clear();
        ApiActors.closeStage();
    }
}
