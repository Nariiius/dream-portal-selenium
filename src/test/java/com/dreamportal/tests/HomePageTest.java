package com.dreamportal.tests;

import com.dreamportal.pages.HomePage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.RegisterExtension;

import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class HomePageTest {

    private HomePage homePage;

    @RegisterExtension
    ScreenshotOnFailure screenshotOnFailure = new ScreenshotOnFailure(() -> homePage.getDriver());

    @BeforeEach
    void setUp() {
        homePage = new HomePage(new ChromeDriver());
        homePage.open();
    }

    @AfterEach
    void tearDown() {
        if (homePage.getDriver() != null) {
            homePage.getDriver().quit();
        }
    }

    @Test
    void loadingAnimationAppearsThenDisappears() {

        assertTrue(homePage.isLoadingAnimationVisible(), "Loading animation should be visible on load");

        homePage.waitForLoadingAnimationToDisappear();

        homePage.waitForMainContent();

        homePage.waitForDreamButton();
    }

    @Test
    void myDreamsButtonOpensTwoTabs() {
        String original = homePage.getDriver().getWindowHandle();

        homePage.clickMyDreams();

        homePage.waitForTwoNewWindows();

        boolean sawDiary = false;
        boolean sawTotal = false;
        for (String handle : homePage.getDriver().getWindowHandles()) {
            if (handle.equals(original)) {
                continue;
            }

            homePage.getDriver().switchTo().window(handle);

            if (homePage.isPage1()) {
                sawDiary = true;
            }

            if (homePage.isPage2()) {
                sawTotal = true;
            }
        }

        assertTrue(sawDiary, "No dreams-diary tab was opened");

        assertTrue(sawTotal, "No dreams-total tab was opened");
    }
}
