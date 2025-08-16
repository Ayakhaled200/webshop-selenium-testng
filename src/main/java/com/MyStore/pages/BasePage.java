package com.MyStore.pages;

import com.MyStore.core.Config;
import com.MyStore.core.DriverFactory;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;

/** Common helpers for all pages: a wait, find, click, type. */
public abstract class BasePage {
    protected final WebDriver driver = DriverFactory.get(); // current test’s browser
    protected final WebDriverWait wait =
            new WebDriverWait(driver, Duration.ofSeconds(Config.timeoutSeconds())); // explicit waits


    protected WebElement el(By locator) {
        // Wait until the element is visible, then return it
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected void click(By locator) {
        // 1) Wait until Selenium *thinks* it’s safe to click
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
        try {
            // 2) Try a normal WebDriver click (native browser click)
            element.click();
        } catch (org.openqa.selenium.ElementClickInterceptedException e1) {
            // 3) If something is on top (sticky header/overlay), scroll it into view
            ((org.openqa.selenium.JavascriptExecutor) driver)
                    .executeScript("arguments[0].scrollIntoView({block:'center', inline:'nearest'});", element);
            try {
                // 4) Try the normal click again after scrolling
                element.click();
            } catch (org.openqa.selenium.ElementClickInterceptedException e2) {
                // 5) If it’s *still* intercepted (some layer overlaps), force a JS click
                ((org.openqa.selenium.JavascriptExecutor) driver)
                        .executeScript("arguments[0].click();", element);
            }
        }
    }


    protected void type(By locator, String text) {
        WebElement e = el(locator);
        e.clear();
        e.sendKeys(text);
    }
}
