package com.dreamportal.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class DreamsDiaryPage {

    private final WebDriver driver;

    private final By tableRows = By.xpath("//table[@id='dreamsDiary']/tbody/tr");

    public DreamsDiaryPage(WebDriver driver) {
        this.driver = driver;
    }

    public void open() {
        driver.get("https://arjitnigam.github.io/myDreams/dreams-diary.html");
    }

    public int getRowCount() {
        return driver.findElements(tableRows).size();
    }

    public List<WebElement> getRows() {
        return driver.findElements(tableRows);
    }
}
