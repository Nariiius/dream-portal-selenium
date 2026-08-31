package com.dreamportal.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DreamsTotalPage {

    private final WebDriver driver;

    private final By tableRows = By.xpath("//table[@id='dreamsTotal']/tbody/tr");

    public DreamsTotalPage(WebDriver driver) {
        this.driver = driver;
    }

    public void open() {
        driver.get("https://arjitnigam.github.io/myDreams/dreams-total.html");
    }

    public Map<String, String> getStats() {
        Map<String, String> stats = new HashMap<>();
        for (WebElement row : driver.findElements(tableRows)) {
            List<WebElement> cells = row.findElements(By.tagName("td"));

            stats.put(cells.get(0).getText(), cells.get(1).getText());
        }
        return stats;
    }

    public int getRecurringCount() {
        return Integer.parseInt(getStats().get("Recurring Dreams"));
    }
}
