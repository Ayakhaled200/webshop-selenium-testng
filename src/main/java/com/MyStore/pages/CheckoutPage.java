package com.MyStore.pages;

import org.openqa.selenium.By;

public class CheckoutPage extends BasePage {


    private final By deliveryBox = By.cssSelector("#address_delivery, #delivery_address");
    private final By billingBox  = By.cssSelector("#address_invoice, #billing_address");

    private final By addressHeader = By.xpath(
            "//*[contains(normalize-space(),'Address Details') or contains(normalize-space(),'Review Your Order')]"
    );

    private final By placeOrderBtn   = By.xpath(
            "//a[normalize-space()='Place Order'] | //button[normalize-space()='Place Order']"
    );

    public boolean isLoaded() {
        try { return el(addressHeader).isDisplayed(); } catch (Exception e) { return false; }
    }

    public String deliveryAddressText() { return el(deliveryBox).getText().trim(); }
    public String billingAddressText()  { return el(billingBox).getText().trim(); }

    public PaymentPage placeOrder() {
        click(placeOrderBtn);
        return new PaymentPage();
    }

}
