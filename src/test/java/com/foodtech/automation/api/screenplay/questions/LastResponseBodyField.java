package com.foodtech.automation.api.screenplay.questions;

import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;

public class LastResponseBodyField implements Question<String> {

    private final String field;

    private LastResponseBodyField(String field) {
        this.field = field;
    }

    public static LastResponseBodyField valueOf(String field) {
        return new LastResponseBodyField(field);
    }

    @Override
    public String answeredBy(Actor actor) {
        return SerenityRest.lastResponse().jsonPath().getString(field);
    }
}
