package com.foodtech.automation.api.screenplay.tasks.auth;

import com.foodtech.automation.api.screenplay.support.api.ApiRoutes;
import com.foodtech.automation.api.screenplay.support.context.CrudExecutionContext;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.rest.interactions.Post;

import java.time.Instant;
import java.util.Map;

public class AuthenticateUser implements Task {

    public static AuthenticateUser withDefaultCredentials() {
        return Tasks.instrumented(AuthenticateUser.class);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        String suffix = String.valueOf(Instant.now().toEpochMilli());
        String username = "api-user-" + suffix;
        String email = "api.user." + suffix + "@foodtech.com";
        String password = "ApiPass123";

        actor.attemptsTo(
            Post.to("/api/auth/register")
                .with(request -> request
                    .contentType("application/json")
                    .body(Map.of(
                        "username", username,
                        "email", email,
                        "password", password
                    ))
                )
        );

        int registerStatus = SerenityRest.lastResponse().statusCode();
        if (registerStatus < 200 || registerStatus > 299) {
            throw new IllegalStateException(
                "Setup failed: actor self-registration returned " + registerStatus);
        }

        actor.attemptsTo(
                Post.to(ApiRoutes.LOGIN)
                        .with(request -> request
                                .contentType("application/json")
                                .body(Map.of(
                        "identifier", email,
                        "password", password
                                ))
                        )
        );

        int loginStatus = SerenityRest.lastResponse().statusCode();
        String token = SerenityRest.lastResponse().jsonPath().getString("token");
        if (loginStatus != 200 || token == null || token.isBlank()) {
            throw new IllegalStateException(
                "Setup failed: login did not return a valid token");
        }
        CrudExecutionContext.current().setToken(token);
    }
}
