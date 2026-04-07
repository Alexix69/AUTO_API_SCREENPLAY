package com.foodtech.automation.api.screenplay.tasks.authorization;

import com.foodtech.automation.api.screenplay.support.context.AuthorizationExecutionContext;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Get;
import net.serenitybdd.screenplay.rest.interactions.Post;

import java.util.Collections;

public class CallUnauthorizedEndpoint implements Task {

    private final String method;
    private final String path;

    private CallUnauthorizedEndpoint(String method, String path) {
        this.method = method;
        this.path = path;
    }

    public static CallUnauthorizedEndpoint post(String path) {
        return new CallUnauthorizedEndpoint("POST", path);
    }

    public static CallUnauthorizedEndpoint get(String path) {
        return new CallUnauthorizedEndpoint("GET", path);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        String token = AuthorizationExecutionContext.current().token();
        if ("POST".equals(method)) {
            actor.attemptsTo(
                    Post.to(path)
                            .with(request -> request
                                    .contentType("application/json")
                                    .header("Authorization", "Bearer " + token)
                                    .body(Collections.emptyMap()))
            );
        } else {
            actor.attemptsTo(
                    Get.resource(path)
                            .with(request -> request
                                    .header("Authorization", "Bearer " + token))
            );
        }
    }
}
