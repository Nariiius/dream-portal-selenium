package com.dreamportal.tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RecurringDreamsTest {

    private WebDriver driver;

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
        // counting how often each dream name appears in the diary
        driver.get("https://arjitnigam.github.io/myDreams/dreams-diary.html");
        Map<String, Integer> nameCounts = new HashMap<>();
        List<WebElement> diaryRows = driver.findElements(By.xpath("//table[@id='dreamsDiary']/tbody/tr"));
        for (WebElement row : diaryRows) {
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

        // checking the dreams total page for the count
        driver.get("https://arjitnigam.github.io/myDreams/dreams-total.html");
        Map<String, String> stats = new HashMap<>();
        List<WebElement> summaryRows = driver.findElements(By.xpath("//table[@id='dreamsTotal']/tbody/tr"));
        for (WebElement row : summaryRows) {
            List<WebElement> cells = row.findElements(By.tagName("td"));
            stats.put(cells.get(0).getText(), cells.get(1).getText());
        }
        int summaryRecurring = Integer.parseInt(stats.get("Recurring Dreams"));

        assertEquals(recurring, summaryRecurring, "Recurring computed from diary (" + recurring + ") should match summary (" + summaryRecurring + ")");
    }
}
