//package com.MyStore.pages;
//import org.openqa.selenium.By;
//import org.openqa.selenium.WebDriver;
//
//public class ConfirmationPage {
//    private WebDriver driver;
//
//    public ConfirmationPage(WebDriver driver) {
//        this.driver = driver;
//    }
//
//    //14 - Account Created Message
//    private final By account_created_msg = By.xpath("//b[text()='Account Created!']");
//
//    //15 - Continue Button
//    private final By continue_button = By.xpath("//a[@data-qa='continue-button']");
//
//    //18 - is account deleted
//    private final By accountDeletedMessage = By.xpath("//b[text()='Account Deleted!']");
//
//    //14
//    public boolean verifyAccountCreated() {
//       return driver.findElement(account_created_msg).isDisplayed();
//    }
//
//    //15
//    public void clickContinue() {
//        driver.findElement(continue_button).click();
//    }
//
//    //18
//    public boolean isAccountDeletedVisible() {
//        return driver.findElement(accountDeletedMessage).isDisplayed();
//    }
//}


package com.MyStore.pages;

import org.openqa.selenium.By;

public class AccountCreatedPage extends BasePage {
    private final By header =
            By.xpath("//*[normalize-space()='Account Created!' or normalize-space()='ACCOUNT CREATED!']");
    private final By continueBtn = By.cssSelector("a[data-qa='continue-button']");

    public boolean isLoaded() {
        try { return el(header).isDisplayed(); }
        catch (Exception e) { return false; }
    }

    /** Clicks "Continue" and returns Home. */
    public HomePage continueToHome() {
        click(continueBtn);
        return new HomePage();
    }
}
