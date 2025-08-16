package com.MyStore.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ContactUsPage extends BasePage {
    // Header
    private final By header = By.xpath("//*[normalize-space()='Get In Touch' or normalize-space()='GET IN TOUCH']");

    // Form fields
    private final By name    = By.name("name");
    private final By email   = By.name("email");
    private final By subject = By.name("subject");
    private final By message = By.xpath("//textarea[@id='message' or @name='message']");
    private final By upload  = By.name("upload_file"); // site uses 'upload_file'

    // Submit: handle both <button> and <input type=submit value="Submit">
    private final By submitBtn = By.xpath(
            "//button[normalize-space()='Submit']" +
                    " | //input[@type='submit' and contains(translate(@value,'submit','SUBMIT'),'SUBMIT')]"
    );

    // Success banner
    private final By successBanner = By.xpath(
            "//*[contains(normalize-space(),'Success! Your details have been submitted successfully')]"
    );

    public boolean isLoaded() { return el(header).isDisplayed(); }

    public ContactUsPage fill(String yourName, String yourEmail, String subj, String msg) {
        type(name, yourName);
        type(email, yourEmail);
        type(subject, subj);
        type(message, msg);
        return this;
    }

    public ContactUsPage upload(String absolutePath) {
        type(upload, absolutePath); // sendKeys to file input
        return this;
    }

    public ContactUsPage submit() {
        WebElement btn = el(submitBtn);

        // Bring into view, then try normal click, then JS fallback
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", btn);
        try {
            click(submitBtn);
        } catch (Exception ignored) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
        }

        // Handle the alert("Press OK to submit!") that appears after clicking
        try {
            new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.alertIsPresent());
            driver.switchTo().alert().accept();
        } catch (TimeoutException ignored) {}

        return this;
    }

    public boolean successVisible() {
        try { return el(successBanner).isDisplayed(); }
        catch (Exception e) { return false; }
    }
}
