package com.dreamportal.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class HomePage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By loadingAnimation = By.id("loadingAnimation");
    private final By mainContent = By.id("mainContent");
    private final By dreamButton = By.id("dreamButton");

    private final String pageName1 = "dreams-diary";
    private final String pageName2 = "dreams-total";

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void open() {
        driver.get("https://arjitnigam.github.io/myDreams/");
    }

    public boolean isLoadingAnimationVisible() {
        return driver.findElement(loadingAnimation).isDisplayed();
    }

    public void waitForLoadingAnimationToDisappear() {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingAnimation));
    }

    public void waitForMainContent() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(mainContent));
    }

    public void waitForDreamButton() {
        wait.until(ExpectedConditions.elementToBeClickable(dreamButton));
    }

    public void clickMyDreams() {
        wait.until(ExpectedConditions.elementToBeClickable(dreamButton)).click();
    }

    public void waitForTwoNewWindows() {
        wait.until(ExpectedConditions.numberOfWindowsToBe(3));
    }

    public boolean isPage1() {
        return driver.getCurrentUrl().contains(pageName1);
    }

    public boolean isPage2() {
        return driver.getCurrentUrl().contains(pageName2);
    }

    public WebDriver getDriver() {
        return driver;
    }
}
