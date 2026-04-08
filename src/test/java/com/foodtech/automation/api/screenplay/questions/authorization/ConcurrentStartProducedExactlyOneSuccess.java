package com.foodtech.automation.api.screenplay.questions.authorization;

import com.foodtech.automation.api.screenplay.support.model.ConcurrentStartResult;
import com.foodtech.automation.api.screenplay.tasks.authorization.ExecuteConcurrentTaskStart;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;

public class ConcurrentStartProducedExactlyOneSuccess implements Question<Boolean> {

    public static ConcurrentStartProducedExactlyOneSuccess andOneConflict() {
        return new ConcurrentStartProducedExactlyOneSuccess();
    }

    @Override
    public Boolean answeredBy(Actor actor) {
        ConcurrentStartResult result = ExecuteConcurrentTaskStart.lastResult();
        if (result == null) {
            return false;
        }
        int first = result.firstStatus();
        int second = result.secondStatus();
        return (first == 200 && second == 409) || (first == 409 && second == 200);
    }
}
