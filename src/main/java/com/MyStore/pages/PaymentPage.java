package com.MyStore.pages;

import org.openqa.selenium.By;

public class PaymentPage extends BasePage {
    private final By paymentHeader = By.xpath("//*[contains(normalize-space(),'Payment')]");

    private final By nameOnCard  = By.name("name_on_card");
    private final By cardNumber  = By.name("card_number");
    private final By cvc         = By.name("cvc");
    private final By expiryMonth = By.name("expiry_month");
    private final By expiryYear  = By.name("expiry_year");
    private final By payConfirm  = By.id("submit"); // Pay and Confirm Order

    public boolean isLoaded() {
        try { return el(paymentHeader).isDisplayed(); } catch (Exception e) { return false; }
    }

    public OrderPlacedPage pay(String holder, String num, String cvcVal, String mm, String yy) {
        type(nameOnCard,  holder);
        type(cardNumber,  num);
        type(cvc,         cvcVal);
        type(expiryMonth, mm);
        type(expiryYear,  yy);
        click(payConfirm);
        return new OrderPlacedPage();
    }

    public OrderPlacedPage pay(com.MyStore.data.PaymentCard card) {
        return pay(card.holder(), card.number(), card.cvc(), card.month(), card.year());
    }

}
