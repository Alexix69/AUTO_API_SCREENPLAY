package com.foodtech.automation.api.screenplay.runners;

import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
        features = "features/orders",
        glue = "com.foodtech.automation.api.screenplay.stepdefinitions",
        monochrome = true
)
public class OrdersCrudTestSuite {
}
