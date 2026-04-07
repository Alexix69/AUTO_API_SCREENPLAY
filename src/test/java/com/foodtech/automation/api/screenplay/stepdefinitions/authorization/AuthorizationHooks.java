package com.foodtech.automation.api.screenplay.stepdefinitions.authorization;

import com.foodtech.automation.api.screenplay.support.actors.ApiActors;
import com.foodtech.automation.api.screenplay.support.api.ApiRoutes;
import com.foodtech.automation.api.screenplay.support.context.AuthorizationExecutionContext;
import com.foodtech.automation.api.screenplay.tasks.authorization.AuthenticateUserWithRole;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.rest.interactions.Get;
import net.serenitybdd.screenplay.rest.interactions.Patch;
import net.serenitybdd.screenplay.rest.interactions.Post;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AuthorizationHooks {

    @Before("@apiAuthCocineroOrders")
    public void beforeCocineroOrders() {
        Actor actor = ApiActors.openStage();
        AuthorizationExecutionContext.init();
        actor.attemptsTo(AuthenticateUserWithRole.as("COCINERO"));
    }

    @Before("@apiAuthMeseroBarTasks")
    public void beforeMeseroBarTasks() {
        Actor actor = ApiActors.openStage();
        AuthorizationExecutionContext.init();
        actor.attemptsTo(AuthenticateUserWithRole.as("MESERO"));
    }

    @Before("@apiAuthMeseroTasks")
    public void beforeMeseroTasks() {
        Actor actor = ApiActors.openStage();
        AuthorizationExecutionContext.init();
        actor.attemptsTo(AuthenticateUserWithRole.as("MESERO"));
    }

    @Before("@apiCrossRoleStartForbidden")
    public void beforeCrossRoleStart() {
        Actor actor = ApiActors.openStage();
        AuthorizationExecutionContext ctx = AuthorizationExecutionContext.init();

        String meseroToken = loginNewUser(actor, "MESERO");

        actor.attemptsTo(
                Post.to(ApiRoutes.ORDERS)
                        .with(req -> req
                                .contentType("application/json")
                                .header("Authorization", "Bearer " + meseroToken)
                                .body(Map.of("tableNumber", "T-C201", "products",
                                        List.of(Map.of("name", "Mojito", "type", "DRINK")))))
        );
        long orderId = SerenityRest.lastResponse().jsonPath().getLong("orderId");

        String bartToken = loginNewUser(actor, "BARTENDER");

        actor.attemptsTo(
                Get.resource(ApiRoutes.TASKS_BY_BAR_STATION)
                        .with(req -> req.header("Authorization", "Bearer " + bartToken))
        );
        Long taskId = findTaskIdForOrder(SerenityRest.lastResponse().jsonPath().getList("$"), orderId);

        actor.attemptsTo(AuthenticateUserWithRole.as("COCINERO"));
        ctx.setTaskId(taskId);
    }

    @Before("@apiStartAlreadyInPreparationConflict")
    public void beforeAlreadyInPreparationConflict() {
        Actor actor = ApiActors.openStage();
        AuthorizationExecutionContext ctx = AuthorizationExecutionContext.init();

        String meseroToken = loginNewUser(actor, "MESERO");

        actor.attemptsTo(
                Post.to(ApiRoutes.ORDERS)
                        .with(req -> req
                                .contentType("application/json")
                                .header("Authorization", "Bearer " + meseroToken)
                                .body(Map.of("tableNumber", "T-C202", "products",
                                        List.of(Map.of("name", "Paella", "type", "HOT_DISH")))))
        );
        long orderId = SerenityRest.lastResponse().jsonPath().getLong("orderId");

        actor.attemptsTo(AuthenticateUserWithRole.as("COCINERO"));
        String cocineroToken = ctx.token();

        actor.attemptsTo(
                Get.resource("/api/tasks/station/HOT_KITCHEN")
                        .with(req -> req.header("Authorization", "Bearer " + cocineroToken))
        );
        Long taskId = findTaskIdForOrder(SerenityRest.lastResponse().jsonPath().getList("$"), orderId);

        actor.attemptsTo(
                Patch.to(ApiRoutes.taskStartPath(taskId))
                        .with(req -> req.header("Authorization", "Bearer " + cocineroToken))
        );

        ctx.setTaskId(taskId);
    }

    @After("@apiAuthCocineroOrders or @apiAuthMeseroBarTasks or @apiAuthMeseroTasks or @apiCrossRoleStartForbidden or @apiStartAlreadyInPreparationConflict")
    public void afterAuthorizationScenario() {
        AuthorizationExecutionContext.clear();
        ApiActors.closeStage();
    }

    private String loginNewUser(Actor actor, String role) {
        String suffix = UUID.randomUUID().toString().replace("-", "").substring(0, 12);
        String email = role.toLowerCase() + "." + suffix + "@foodtech.com";
        String password = "Pass" + suffix.substring(0, 6);
        actor.attemptsTo(
                Post.to("/api/auth/register")
                        .with(req -> req
                                .contentType("application/json")
                                .body(Map.of("username", role.toLowerCase() + suffix,
                                        "email", email, "password", password, "role", role)))
        );
        actor.attemptsTo(
                Post.to(ApiRoutes.LOGIN)
                        .with(req -> req
                                .contentType("application/json")
                                .body(Map.of("identifier", email, "password", password)))
        );
        return SerenityRest.lastResponse().jsonPath().getString("token");
    }

    private Long findTaskIdForOrder(List<Map<String, Object>> tasks, long orderId) {
        if (tasks == null) return null;
        for (Map<String, Object> task : tasks) {
            Object taskOrderId = task.get("orderId");
            if (taskOrderId != null && ((Number) taskOrderId).longValue() == orderId) {
                return ((Number) task.get("id")).longValue();
            }
        }
        return null;
    }
}

