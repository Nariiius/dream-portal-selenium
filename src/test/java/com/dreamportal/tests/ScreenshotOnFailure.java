package com.dreamportal.tests;

import io.qameta.allure.Allure;

import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.ByteArrayInputStream;
import java.util.function.Supplier;

//take screenshot while driver is still active and saving to allure report
public class ScreenshotOnFailure implements AfterTestExecutionCallback {

    private final Supplier<WebDriver> driverSupplier;

    public ScreenshotOnFailure(Supplier<WebDriver> driverSupplier) {
        this.driverSupplier = driverSupplier;
    }

    @Override
    public void afterTestExecution(ExtensionContext context) {
        if (context.getExecutionException().isPresent()) {
            WebDriver driver = driverSupplier.get();
            if (driver != null) {
                try {
                    Allure.addAttachment(
                            "Failure screenshot",
                            "image/png",
                            new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)),
                            ".png");
                } catch (Exception e) {
                    // screenshot failure should not show up as a failure of a system.
                }
            }
        }
    }
}
