package com.dreamportal.tests;

import com.dreamportal.pages.DreamsDiaryPage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DreamsDiaryTest {

    private WebDriver driver;
    private DreamsDiaryPage diaryPage;

    @BeforeEach
    void setUp() {
        driver = new ChromeDriver();
        diaryPage = new DreamsDiaryPage(driver);
        diaryPage.open();
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void diaryHasValidEntries() {
        assertEquals(10, diaryPage.getRowCount(), "Expected 10 rows, but found " + diaryPage.getRowCount());

        for (WebElement row : diaryPage.getRows()) {

            List<WebElement> cells = row.findElements(By.tagName("td"));
            assertEquals(3, cells.size(), "Expected 3 cells, but found " + cells.size());

            for (WebElement cell : cells) {
                assertFalse(cell.getText().isBlank(), "Cell text should not be blank");
            }

            String type = cells.get(2).getText();

            assertTrue(type.equals("Good") || type.equals("Bad"), "Type should be Good or Bad, was: " + type);
        }
    }
}
