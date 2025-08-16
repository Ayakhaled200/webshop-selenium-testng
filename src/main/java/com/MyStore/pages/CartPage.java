package com.MyStore.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CartPage extends BasePage {

    private static String xpathLiteral(String s) {
        if (!s.contains("'"))  return "'" + s + "'";
        if (!s.contains("\"")) return "\"" + s + "\"";
        // fallback: concat('foo',"'",'bar',"...")
        return "concat('" + s.replace("'", "',\"'\",'") + "')";
    }

    // ---- Identity ----
    private final By cartTitle = By.xpath(
            "//*[normalize-space()='Shopping Cart' or contains(normalize-space(),'Cart')]"
    );

    // Any row inside the cart table
    private final By cartRows = By.cssSelector("#cart_info_table tbody tr");


    // ---- Rows & helpers (keep your versions) ----
    private String rowXPath(String name) {
        String n = xpathLiteral(name.trim());
        return "//tr[td[contains(normalize-space()," + n + ")]]";
    }

    private By rowByProduct(String name) {
        return By.xpath(rowXPath(name));
    }

    private By qtyCell(String name) {
        String n = xpathLiteral(name.trim());
        return By.xpath(
                "//tr[td[contains(normalize-space()," + n + ")]]//input[contains(@class,'cart_quantity_input')]"
                        + " | //tr[td[contains(normalize-space()," + n + ")]]//button[contains(@class,'disabled')]"
        );
    }

    private By removeBtn(String name) {
        String n = xpathLiteral(name.trim());
        return By.xpath("//tr[td[contains(normalize-space()," + n + ")]]//a[contains(@class,'cart_quantity_delete')]");
    }

    private By unitPriceCell(String name) {
        return By.xpath(rowXPath(name) + "//td[contains(@class,'cart_price')]//*[self::p or self::span]");
    }
    private By lineTotalCell(String name) {
        return By.xpath(rowXPath(name) + "//td[contains(@class,'cart_total')]//*[self::p or self::span]");
    }

    // empty state
    private final By emptyBanner = By.xpath(
            "//*[contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'cart is empty')]"
    );

    // proceed buttons (you had proceed; adding anonymous path + alias)
    private final By proceedToCheckout = By.xpath(
            "//a[normalize-space()='Proceed To Checkout' or normalize-space()='Proceed to Checkout']"
    );
    private final By registerLoginLinkInModal = By.xpath("//u[normalize-space()='Register / Login']/..");

    // footer subscribe (new)
    private final By subscribeEmail = By.id("susbscribe_email"); // (sic)
    private final By subscribeBtn   = By.id("subscribe");
    private final By subscribeOk    = By.xpath(
            "//*[contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'successfully subscribed')]"
    );

    // ---- Queries ----
    public boolean isLoaded() {
        return el(cartTitle).isDisplayed();
    }
    public boolean hasProduct(String name) {
        try { return el(rowByProduct(name)).isDisplayed(); }
        catch (Exception ex) { return false; }
    }

    public boolean subscriptionSuccessVisible() {
        return subscriptionSuccess();
    }
    public int getQuantity(String productName) {            // your original API (kept)
        var e = el(qtyCell(productName));
        String txt = e.getAttribute("value");
        if (txt == null || txt.isBlank()) txt = e.getText();
        txt = txt.replaceAll("[^0-9]", "").trim();
        return txt.isEmpty() ? 0 : Integer.parseInt(txt);
    }
    public int quantityOf(String productName) {             // friendly alias
        return getQuantity(productName);
    }

    public int unitPriceOf(String productName) {
        String txt = el(unitPriceCell(productName)).getText();
        txt = txt.replaceAll("[^0-9]", "");
        return txt.isEmpty() ? 0 : Integer.parseInt(txt);
    }

    public int lineTotalOf(String productName) {
        String txt = el(lineTotalCell(productName)).getText();
        txt = txt.replaceAll("[^0-9]", "");
        return txt.isEmpty() ? 0 : Integer.parseInt(txt);
    }

    public boolean isEmpty() {
        // Explicit empty banner → empty
        if (!driver.findElements(emptyBanner).isEmpty()) return true;

        // If the table has at least one row → NOT empty
        if (!driver.findElements(cartRows).isEmpty()) return false;

        // Fallback: look for any typical cart columns
        boolean hasAnyRow = !driver.findElements(
                By.xpath("//tr[td[contains(@class,'cart_description')] " +
                        "or td[contains(@class,'cart_price')] " +
                        "or td[contains(@class,'cart_quantity')]]")
        ).isEmpty();

        return !hasAnyRow;
    }


    public boolean subscriptionSuccess() {
        try { return el(subscribeOk).isDisplayed(); }
        catch (Exception e) { return false; }
    }

    // ---- Actions ----
    public CartPage remove(String productName) {
        click(removeBtn(productName));
        return this;
    }

    public CartPage removeAndWait(String productName) {     // safer when single item
        click(removeBtn(productName));
        try {
            new WebDriverWait(driver, Duration.ofSeconds(5)).until(
                    ExpectedConditions.or(
                            ExpectedConditions.invisibilityOfElementLocated(rowByProduct(productName)),
                            ExpectedConditions.visibilityOfElementLocated(emptyBanner)
                    )
            );
        } catch (Exception ignored) {}
        return this;
    }

    public CheckoutPage proceed() {
        click(proceedToCheckout);
        return new CheckoutPage();
    }
    public CheckoutPage proceedToCheckout() {               // alias (optional)
        return proceed();
    }

    /** Anonymous path (Register/Login modal or redirect to Auth). */
    public AuthPage proceedToCheckoutExpectingAuth() {
        click(proceedToCheckout);
        try { click(registerLoginLinkInModal); } catch (Exception ignored) {}
        return new AuthPage();
    }

    public CartPage subscribe(String email) {
        type(subscribeEmail, email);
        click(subscribeBtn);
        return this;
    }

}
