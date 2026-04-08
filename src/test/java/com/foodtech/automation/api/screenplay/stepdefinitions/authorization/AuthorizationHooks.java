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
                Get.resource(ApiRoutes.TASKS_BY_HOT_KITCHEN)
                        .with(req -> req.header("Authorization", "Bearer " + cocineroToken))
        );
        Long taskId = findTaskIdForOrder(SerenityRest.lastResponse().jsonPath().getList("$"), orderId);

        actor.attemptsTo(
                Patch.to(ApiRoutes.taskStartPath(taskId))
                        .with(req -> req.header("Authorization", "Bearer " + cocineroToken))
        );

        ctx.setTaskId(taskId);
    }

    @Before("@apiOrderNotCompletedWhenTasksRemain")
    public void beforeOrderNotCompletedWhenTasksRemain() {
        Actor actor = ApiActors.openStage();
        AuthorizationExecutionContext ctx = AuthorizationExecutionContext.init();

        String meseroToken = loginNewUser(actor, "MESERO");

        actor.attemptsTo(
                Post.to(ApiRoutes.ORDERS)
                        .with(req -> req
                                .contentType("application/json")
                                .header("Authorization", "Bearer " + meseroToken)
                                .body(Map.of("tableNumber", "T-C901", "products",
                                        List.of(
                                                Map.of("name", "Paella", "type", "HOT_DISH"),
                                                Map.of("name", "Mojito", "type", "DRINK")
                                        ))))
        );
        long orderId = SerenityRest.lastResponse().jsonPath().getLong("orderId");

        actor.attemptsTo(AuthenticateUserWithRole.as("COCINERO"));
        String cocineroToken = ctx.token();

        actor.attemptsTo(
                Get.resource(ApiRoutes.TASKS_BY_HOT_KITCHEN)
                        .with(req -> req.header("Authorization", "Bearer " + cocineroToken))
        );
        Long cocineroTaskId = findTaskIdForOrder(SerenityRest.lastResponse().jsonPath().getList("$"), orderId);

        actor.attemptsTo(
                Patch.to(ApiRoutes.taskStartPath(cocineroTaskId))
                        .with(req -> req.header("Authorization", "Bearer " + cocineroToken))
        );

        ctx.setTaskId(cocineroTaskId);
        ctx.setOrderId(String.valueOf(orderId));
    }

    @Before("@apiOrderCompletedWhenLastTaskDone")
    public void beforeOrderCompletedWhenLastTaskDone() {
        Actor actor = ApiActors.openStage();
        AuthorizationExecutionContext ctx = AuthorizationExecutionContext.init();

        String meseroToken = loginNewUser(actor, "MESERO");

        actor.attemptsTo(
                Post.to(ApiRoutes.ORDERS)
                        .with(req -> req
                                .contentType("application/json")
                                .header("Authorization", "Bearer " + meseroToken)
                                .body(Map.of("tableNumber", "T-C902", "products",
                                        List.of(
                                                Map.of("name", "Paella", "type", "HOT_DISH"),
                                                Map.of("name", "Mojito", "type", "DRINK")
                                        ))))
        );
        long orderId = SerenityRest.lastResponse().jsonPath().getLong("orderId");

        String cocineroToken = loginNewUser(actor, "COCINERO");

        actor.attemptsTo(
                Get.resource(ApiRoutes.TASKS_BY_HOT_KITCHEN)
                        .with(req -> req.header("Authorization", "Bearer " + cocineroToken))
        );
        Long cocineroTaskId = findTaskIdForOrder(SerenityRest.lastResponse().jsonPath().getList("$"), orderId);

        actor.attemptsTo(
                Patch.to(ApiRoutes.taskStartPath(cocineroTaskId))
                        .with(req -> req.header("Authorization", "Bearer " + cocineroToken))
        );
        actor.attemptsTo(
                Patch.to(ApiRoutes.taskCompletePath(cocineroTaskId))
                        .with(req -> req.header("Authorization", "Bearer " + cocineroToken))
        );

        actor.attemptsTo(AuthenticateUserWithRole.as("BARTENDER"));
        String bartenderToken = ctx.token();

        actor.attemptsTo(
                Get.resource(ApiRoutes.TASKS_BY_BAR_STATION)
                        .with(req -> req.header("Authorization", "Bearer " + bartenderToken))
        );
        Long bartenderTaskId = findTaskIdForOrder(SerenityRest.lastResponse().jsonPath().getList("$"), orderId);

        actor.attemptsTo(
                Patch.to(ApiRoutes.taskStartPath(bartenderTaskId))
                        .with(req -> req.header("Authorization", "Bearer " + bartenderToken))
        );

        ctx.setTaskId(bartenderTaskId);
        ctx.setOrderId(String.valueOf(orderId));
    }

    @Before("@apiCompleteTaskInPendingReturns409")
    public void beforeCompleteTaskInPendingReturns409() {
        Actor actor = ApiActors.openStage();
        AuthorizationExecutionContext ctx = AuthorizationExecutionContext.init();

        String meseroToken = loginNewUser(actor, "MESERO");

        actor.attemptsTo(
                Post.to(ApiRoutes.ORDERS)
                        .with(req -> req
                                .contentType("application/json")
                                .header("Authorization", "Bearer " + meseroToken)
                                .body(Map.of("tableNumber", "T-C903", "products",
                                        List.of(Map.of("name", "Paella", "type", "HOT_DISH")))))
        );
        long orderId = SerenityRest.lastResponse().jsonPath().getLong("orderId");

        actor.attemptsTo(AuthenticateUserWithRole.as("COCINERO"));
        String cocineroToken = ctx.token();

        actor.attemptsTo(
                Get.resource(ApiRoutes.TASKS_BY_HOT_KITCHEN)
                        .with(req -> req.header("Authorization", "Bearer " + cocineroToken))
        );
        Long taskId = findTaskIdForOrder(SerenityRest.lastResponse().jsonPath().getList("$"), orderId);

        ctx.setTaskId(taskId);
    }

    @Before("@concurrentStartRaceCondition")
    public void beforeConcurrentStartRaceCondition() {
        Actor actor = ApiActors.openStage();
        AuthorizationExecutionContext ctx = AuthorizationExecutionContext.init();

        String meseroToken = loginNewUser(actor, "MESERO");

        actor.attemptsTo(
                Post.to(ApiRoutes.ORDERS)
                        .with(req -> req
                                .contentType("application/json")
                                .header("Authorization", "Bearer " + meseroToken)
                                .body(Map.of("tableNumber", "T-C904", "products",
                                        List.of(Map.of("name", "Paella", "type", "HOT_DISH")))))
        );
        long orderId = SerenityRest.lastResponse().jsonPath().getLong("orderId");

        String token1 = loginNewUser(actor, "COCINERO");
        String token2 = loginNewUser(actor, "COCINERO");

        actor.attemptsTo(
                Get.resource(ApiRoutes.TASKS_BY_HOT_KITCHEN)
                        .with(req -> req.header("Authorization", "Bearer " + token1))
        );
        Long taskId = findTaskIdForOrder(SerenityRest.lastResponse().jsonPath().getList("$"), orderId);

        ctx.setToken(token1);
        ctx.setSecondToken(token2);
        ctx.setTaskId(taskId);
    }

    @Before("@apiInvalidRoleRegistration")
    public void beforeInvalidRoleRegistration() {
        Actor actor = ApiActors.openStage();
        actor.attemptsTo(
                Post.to(ApiRoutes.REGISTER)
                        .with(req -> req
                                .contentType("application/json")
                                .body(Map.of(
                                        "username", "invalid-role-user",
                                        "email", "invalid.role@test.com",
                                        "password", "TestPass123",
                                        "role", "INVALID_ROLE"
                                )))
        );
    }

    @After("@apiAuthCocineroOrders or @apiAuthMeseroBarTasks or @apiAuthMeseroTasks or @apiCrossRoleStartForbidden or @apiStartAlreadyInPreparationConflict or @apiOrderNotCompletedWhenTasksRemain or @apiOrderCompletedWhenLastTaskDone or @apiCompleteTaskInPendingReturns409 or @concurrentStartRaceCondition or @apiInvalidRoleRegistration")
    public void afterAuthorizationScenario() {
        AuthorizationExecutionContext.clear();
        ApiActors.closeStage();
    }

    private String loginNewUser(Actor actor, String role) {
        String suffix = UUID.randomUUID().toString().replace("-", "").substring(0, 12);
        String email = role.toLowerCase() + "." + suffix + "@foodtech.com";
        String password = "Pass" + suffix.substring(0, 6);
        actor.attemptsTo(
                Post.to(ApiRoutes.REGISTER)
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

