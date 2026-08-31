package com.dreamportal.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DreamsDiaryPage {

    private final WebDriver driver;
    private static final Logger log = LoggerFactory.getLogger(DreamsDiaryPage.class);

    private final By tableRows = By.xpath("//table[@id='dreamsDiary']/tbody/tr");

    public DreamsDiaryPage(WebDriver driver) {
        this.driver = driver;
    }

    public void open() {
        log.info("Opening dreams diary");
        driver.get("https://arjitnigam.github.io/myDreams/dreams-diary.html");
    }

    public int getRowCount() {
        return driver.findElements(tableRows).size();
    }

    public List<WebElement> getRows() {
        return driver.findElements(tableRows);
    }
}
