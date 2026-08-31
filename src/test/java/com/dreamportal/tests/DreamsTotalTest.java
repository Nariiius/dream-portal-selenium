package com.dreamportal.tests;

import com.dreamportal.pages.DreamsTotalPage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.RegisterExtension;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DreamsTotalTest {

    private WebDriver driver;
    private DreamsTotalPage totalPage;

    @RegisterExtension
    ScreenshotOnFailure screenshotOnFailure = new ScreenshotOnFailure(() -> driver);

    @BeforeEach
    void setUp() {
        driver = new ChromeDriver();
        totalPage = new DreamsTotalPage(driver);
        totalPage.open();
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void summaryStatsAreCorrect() {
        Map<String, String> stats = totalPage.getStats();

        assertEquals("6", stats.get("Good Dreams"), "Good Dreams should be 6");
        assertEquals("4", stats.get("Bad Dreams"), "Bad Dreams should be 4");
        assertEquals("10", stats.get("Total Dreams"), "Total Dreams should be 10");
        assertEquals("7", stats.get("Dreams This Week"), "Dreams This Week should be 7");
        assertEquals("2", stats.get("Recurring Dreams"), "Recurring Dreams should be 2");
    }
}
