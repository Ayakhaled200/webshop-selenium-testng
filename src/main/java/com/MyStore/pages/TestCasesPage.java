package com.MyStore.pages;

import org.openqa.selenium.By;

public class TestCasesPage extends BasePage {
    private final By header = By.xpath("//*[normalize-space()='Test Cases' or normalize-space()='TEST CASES']");
    public boolean isLoaded() { return el(header).isDisplayed(); }
}
