package com.foodtech.automation.api.screenplay.stepdefinitions.orders;

import com.foodtech.automation.api.screenplay.questions.LastResponseBodyField;
import com.foodtech.automation.api.screenplay.questions.LastResponseStatus;
import net.serenitybdd.screenplay.Actor;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public final class OrdersCrudAssertions {

    private OrdersCrudAssertions() {
    }

    public static void shouldHaveStatus(Actor actor, int expectedStatus) {
        actor.should(seeThat(LastResponseStatus.code(), equalTo(expectedStatus)));
    }

    public static void shouldContainField(Actor actor, String field) {
        actor.should(seeThat(LastResponseBodyField.valueOf(field), notNullValue()));
    }

    public static void shouldHaveFieldValue(Actor actor, String field, String expectedValue) {
        actor.should(seeThat(LastResponseBodyField.valueOf(field), equalTo(expectedValue)));
    }
}
