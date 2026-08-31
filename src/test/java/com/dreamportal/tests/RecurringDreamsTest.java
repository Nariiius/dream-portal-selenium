package com.dreamportal.tests;

import com.dreamportal.pages.DreamsDiaryPage;
import com.dreamportal.pages.DreamsTotalPage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.RegisterExtension;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RecurringDreamsTest {

    private WebDriver driver;

    @RegisterExtension
    ScreenshotOnFailure screenshotOnFailure = new ScreenshotOnFailure(() -> driver);

    @BeforeEach
    void setUp() {
        driver = new ChromeDriver();
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void recurringDreamsLogicMatchesSummary() {
        DreamsDiaryPage diaryPage = new DreamsDiaryPage(driver);
        diaryPage.open();

        Map<String, Integer> nameCounts = new HashMap<>();
        for (WebElement row : diaryPage.getRows()) {
            List<WebElement> cells = row.findElements(By.tagName("td"));
            String name = cells.get(0).getText();
            nameCounts.put(name, nameCounts.getOrDefault(name, 0) + 1);
        }

        int recurring = 0;
        for (int count : nameCounts.values()) {
            if (count > 1) {
                recurring++;
            }
        }

        assertTrue(nameCounts.getOrDefault("Flying over mountains", 0) > 1, "Flying over mountains should appear more than once");
        assertTrue(nameCounts.getOrDefault("Lost in maze", 0) > 1, "Lost in maze should appear more than once");

        DreamsTotalPage totalPage = new DreamsTotalPage(driver);
        totalPage.open();

        int summaryRecurring = totalPage.getRecurringCount();

        assertEquals(recurring, summaryRecurring, "Recurring computed from diary (" + recurring + ") should match summary (" + summaryRecurring + ")");
    }
}
