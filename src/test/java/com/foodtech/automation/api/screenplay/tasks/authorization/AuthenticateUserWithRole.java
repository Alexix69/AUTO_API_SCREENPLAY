package com.foodtech.automation.api.screenplay.tasks.authorization;

import com.foodtech.automation.api.screenplay.support.api.ApiRoutes;
import com.foodtech.automation.api.screenplay.support.context.AuthorizationExecutionContext;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Post;

import java.time.Instant;
import java.util.Map;

public class AuthenticateUserWithRole implements Task {

    private final String role;

    private AuthenticateUserWithRole(String role) {
        this.role = role;
    }

    public static AuthenticateUserWithRole as(String role) {
        return new AuthenticateUserWithRole(role);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        String suffix = String.valueOf(Instant.now().toEpochMilli());
        String username = "auth-user-" + suffix;
        String email = "auth.user." + suffix + "@foodtech.com";
        String password = "AuthPass123";

        actor.attemptsTo(
                Post.to(ApiRoutes.REGISTER)
                        .with(request -> request
                                .contentType("application/json")
                                .body(Map.of(
                                        "username", username,
                                        "email", email,
                                        "password", password,
                                        "role", role
                                ))
                        )
        );

        int registerStatus = SerenityRest.lastResponse().statusCode();
        if (registerStatus < 200 || registerStatus > 299) {
            throw new IllegalStateException(
                    "Setup failed: registration returned " + registerStatus);
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

        AuthorizationExecutionContext.current().setToken(token);
    }
}
