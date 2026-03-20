package com.foodtech.automation.api.screenplay.stepdefinitions.hooks;

import com.foodtech.automation.api.screenplay.support.actors.ApiActors;
import com.foodtech.automation.api.screenplay.support.context.CrudExecutionContext;
import com.foodtech.automation.api.screenplay.support.data.OrderTestDataFactory;
import com.foodtech.automation.api.screenplay.support.model.OrderPayload;
import io.cucumber.java.After;
import io.cucumber.java.Before;

public class OrdersCrudHooks {

    @Before("@ordersCrud or @ordersDeterministic")
    public void setUpScenarioContext() {
        ApiActors.openStage();

        OrderPayload createPayload = OrderTestDataFactory.createPayload();
        OrderPayload updatePayload = OrderTestDataFactory.updatePayloadFrom(createPayload);
        CrudExecutionContext.init(createPayload, updatePayload);
    }

    @After("@ordersCrud or @ordersDeterministic")
    public void clearScenarioContext() {
        CrudExecutionContext.clear();
        ApiActors.closeStage();
    }
}
