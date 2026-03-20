package com.foodtech.automation.api.screenplay.support.actors;

import com.foodtech.automation.api.screenplay.support.config.TestEnvironment;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;

public final class ApiActors {

    private static final String DEFAULT_ACTOR_NAME = "qa automation engineer";

    private ApiActors() {
    }

    public static Actor openStage() {
        OnStage.setTheStage(new OnlineCast());
        Actor actor = OnStage.theActorCalled(DEFAULT_ACTOR_NAME);
        actor.can(CallAnApi.at(TestEnvironment.apiBaseUrl()));
        return actor;
    }

    public static Actor spotlight() {
        return OnStage.theActorInTheSpotlight();
    }

    public static void closeStage() {
        OnStage.drawTheCurtain();
    }
}
