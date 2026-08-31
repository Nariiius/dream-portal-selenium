package com.dreamportal.tests;

import com.dreamportal.ai.AiClassifier;
import com.dreamportal.ai.Env;
import com.dreamportal.pages.DreamsDiaryPage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.opentest4j.TestAbortedException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class AiDreamClassificationTest {

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
    void aiClassifiesEveryDreamSameAsTable() {
        if (Env.get("OPENROUTER_API_KEY") == null) {
            throw new TestAbortedException("Skipping AI test: OPENROUTER_API_KEY not set");
        }

        DreamsDiaryPage diary = new DreamsDiaryPage(driver);
        diary.open();

        Map<String, String> table = new LinkedHashMap<>();
        for (WebElement row : diary.getRows()) {
            List<WebElement> cells = row.findElements(By.tagName("td"));
            table.put(cells.get(0).getText(), cells.get(2).getText());
        }

        AiClassifier ai = new AiClassifier();
        Map<String, String> aiResult = ai.classify(table.keySet().toArray(new String[0]));

        assertEquals(table.size(), aiResult.size(),
                "AI returned " + aiResult.size() + " classifications, expected " + table.size());

        for (Map.Entry<String, String> e : table.entrySet()) {
            assertEquals(e.getValue(), aiResult.get(e.getKey()),
                    "AI said '" + aiResult.get(e.getKey()) + "' for '" + e.getKey()
                            + "' but the table says '" + e.getValue() + "'");
        }
    }
}
