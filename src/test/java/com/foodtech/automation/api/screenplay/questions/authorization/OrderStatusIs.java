package com.foodtech.automation.api.screenplay.questions.authorization;

import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;

public class OrderStatusIs implements Question<Boolean> {

    private final String expected;

    private OrderStatusIs(String expected) {
        this.expected = expected;
    }

    public static OrderStatusIs equalTo(String status) {
        return new OrderStatusIs(status);
    }

    @Override
    public Boolean answeredBy(Actor actor) {
        String actual = SerenityRest.lastResponse().jsonPath().getString("status");
        return expected.equals(actual);
    }
}
