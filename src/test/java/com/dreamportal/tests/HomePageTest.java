package com.dreamportal.tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class HomePageTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    void setUp() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get("https://arjitnigam.github.io/myDreams/");
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void loadingAnimationAppearsThenDisappears() {

        assertTrue(driver.findElement(By.id("loadingAnimation")).isDisplayed(),
                "Loading animation should be visible on load");

        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loadingAnimation")));

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("mainContent")));

        wait.until(ExpectedConditions.elementToBeClickable(By.id("dreamButton")));
    }

    @Test
    void myDreamsButtonOpensTwoTabs() {
        String original = driver.getWindowHandle();

        wait.until(ExpectedConditions.elementToBeClickable(By.id("dreamButton"))).click();
        wait.until(ExpectedConditions.numberOfWindowsToBe(3));

        for (String handle : driver.getWindowHandles()) {
            if (handle.equals(original)) {
                continue;
            }

            driver.switchTo().window(handle);

            assertTrue(driver.getCurrentUrl().contains("dreams-diary") || driver.getCurrentUrl().contains("dreams-total"),
                "Unexpected URL in new tab: " + driver.getCurrentUrl());
        }
    }
}
