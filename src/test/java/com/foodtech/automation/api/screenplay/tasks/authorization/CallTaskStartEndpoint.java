package com.foodtech.automation.api.screenplay.tasks.authorization;

import com.foodtech.automation.api.screenplay.support.api.ApiRoutes;
import com.foodtech.automation.api.screenplay.support.context.AuthorizationExecutionContext;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Patch;

public class CallTaskStartEndpoint implements Task {

    private final Long taskId;

    private CallTaskStartEndpoint(Long taskId) {
        this.taskId = taskId;
    }

    public static CallTaskStartEndpoint forTask(Long taskId) {
        return new CallTaskStartEndpoint(taskId);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        String token = AuthorizationExecutionContext.current().token();
        actor.attemptsTo(
                Patch.to(ApiRoutes.taskStartPath(taskId))
                        .with(request -> request
                                .header("Authorization", "Bearer " + token))
        );
    }
}
