package com.MyStore.pages;

import org.openqa.selenium.By;

public class OrderPlacedPage extends BasePage {
    private final By successBanner   = By.xpath("//*[contains(normalize-space(),'Order Placed') or contains(normalize-space(),'successfully')]");
    private final By downloadInvoice = By.xpath("//a[normalize-space()='Download Invoice']");
    private final By continueBtn     = By.xpath("//a[normalize-space()='Continue'] | //button[normalize-space()='Continue']");

    public boolean isLoaded() {
        try { return el(successBanner).isDisplayed(); } catch (Exception e) { return false; }
    }

    public OrderPlacedPage downloadInvoice() {
        click(downloadInvoice);
        return this;
    }

    public HomePage continueToHome() {
        click(continueBtn);
        return new HomePage();
    }
}
