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

public class DreamsTotalTest {

    private WebDriver driver;

    @BeforeEach
    void setUp() {
        driver = new ChromeDriver();
        driver.get("https://arjitnigam.github.io/myDreams/dreams-total.html");
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void summaryStatsAreCorrect() {
        List<WebElement> rows = driver.findElements(By.xpath("//table[@id='dreamsTotal']/tbody/tr"));

        Map<String, String> stats = new HashMap<>();
        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(By.tagName("td"));
            stats.put(cells.get(0).getText(), cells.get(1).getText());
        }

        assertEquals("6", stats.get("Good Dreams"), "Good Dreams should be 6");
        assertEquals("4", stats.get("Bad Dreams"), "Bad Dreams should be 4");
        assertEquals("10", stats.get("Total Dreams"), "Total Dreams should be 10");
        assertEquals("7", stats.get("Dreams This Week"), "Dreams This Week should be 7");
        assertEquals("2", stats.get("Recurring Dreams"), "Recurring Dreams should be 2");
    }
}
