package com.foodtech.automation.api.screenplay.questions.authorization;

import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;

public class ResponseStatusIs implements Question<Boolean> {

    private final int expected;

    private ResponseStatusIs(int expected) {
        this.expected = expected;
    }

    public static ResponseStatusIs equalTo(int status) {
        return new ResponseStatusIs(status);
    }

    @Override
    public Boolean answeredBy(Actor actor) {
        return SerenityRest.lastResponse().statusCode() == expected;
    }
}
