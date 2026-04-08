package com.foodtech.automation.api.screenplay.tasks.authorization;

import com.foodtech.automation.api.screenplay.support.config.TestEnvironment;
import com.foodtech.automation.api.screenplay.support.context.AuthorizationExecutionContext;
import com.foodtech.automation.api.screenplay.support.model.ConcurrentStartResult;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public class ExecuteConcurrentTaskStart implements Task {

    private static final ThreadLocal<ConcurrentStartResult> LAST_RESULT = new ThreadLocal<>();

    public static ExecuteConcurrentTaskStart onSameTask() {
        return Tasks.instrumented(ExecuteConcurrentTaskStart.class);
    }

    public static ConcurrentStartResult lastResult() {
        return LAST_RESULT.get();
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        String token1 = AuthorizationExecutionContext.current().token();
        String token2 = AuthorizationExecutionContext.current().secondToken();
        Long taskId = AuthorizationExecutionContext.current().taskId();
        String url = TestEnvironment.apiBaseUrl() + "/api/tasks/" + taskId + "/start";

        CompletableFuture<Integer> first = CompletableFuture.supplyAsync(() -> patchWithToken(url, token1));
        CompletableFuture<Integer> second = CompletableFuture.supplyAsync(() -> patchWithToken(url, token2));

        try {
            CompletableFuture.allOf(first, second).join();
            LAST_RESULT.set(new ConcurrentStartResult(first.get(), second.get()));
        } catch (Exception e) {
            throw new IllegalStateException("Concurrent start execution failed: " + e.getMessage(), e);
        }
    }

    private static int patchWithToken(String url, String token) {
        try {
            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(5))
                    .build();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(10))
                    .header("Authorization", "Bearer " + token)
                    .method("PATCH", HttpRequest.BodyPublishers.noBody())
                    .build();
            return client.send(request, HttpResponse.BodyHandlers.discarding()).statusCode();
        } catch (Exception e) {
            throw new RuntimeException("PATCH request failed: " + e.getMessage(), e);
        }
    }
}
