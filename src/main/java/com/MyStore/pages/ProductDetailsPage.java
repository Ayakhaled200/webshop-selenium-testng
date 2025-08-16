package com.MyStore.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class ProductDetailsPage extends BasePage {

    // Product Details LOCATORS
    private final By name        = By.cssSelector(".product-information h2");
    private final By category    = By.xpath("//div[@class='product-information']//p[contains(.,'Category')]");
    private final By price       = By.xpath("//div[@class='product-information']//span[contains(.,'Rs.') or contains(.,'$')]");
    private final By availability= By.xpath("//div[@class='product-information']//p[contains(.,'Availability')]");
    private final By condition   = By.xpath("//div[@class='product-information']//p[contains(.,'Condition')]");
    private final By brand       = By.xpath("//div[@class='product-information']//p[contains(.,'Brand')]");

    // Product Details getters
    public String productName()       { return el(name).getText().trim(); }
    public String productCategory()   { return el(category).getText().trim(); }
    public String productPrice()      { return el(price).getText().trim(); }
    public String productAvailability(){ return el(availability).getText().trim(); }
    public String productCondition()  { return el(condition).getText().trim(); }
    public String productBrand()      { return el(brand).getText().trim(); }


    // --- Review section locators ---
    private final By reviewHeader  = By.xpath("//*[normalize-space()='Write Your Review']");
    private final By reviewName    = By.id("name");
    private final By reviewEmail   = By.id("email");
    private final By reviewText    = By.id("review");
    private final By reviewSubmit  = By.id("button-review");
    private final By reviewSuccess = By.xpath("//*[contains(normalize-space(),'Thank you for your review')]");

    // Identity: details page shows specs like Category / Availability / Condition
    private final By detailsHeader = By.xpath(
            "//*[contains(normalize-space(),'Product Information') or contains(normalize-space(),'Category') or contains(normalize-space(),'Availability')]"
    );

    // Quantity + add to cart + modal "View Cart"
    private final By qtyInput           = By.id("quantity");
    private final By addToCartBtn       = By.xpath("//button[normalize-space()='Add to cart']");
    private final By viewCartLinkInModal= By.xpath("//a[normalize-space()='View Cart']");

    public boolean isLoaded() {
        return el(detailsHeader).isDisplayed();
    }

    public ProductDetailsPage setQuantity(int qty) {
        WebElement e = el(qtyInput);
        e.clear();
        e.sendKeys(String.valueOf(qty));
        return this;
    }

    public CartPage addToCartGoToCart() {
        click(addToCartBtn);
        el(viewCartLinkInModal);      // wait modal
        click(viewCartLinkInModal);   // go to cart
        return new CartPage();
    }

    // --- Review actions/queries ---
    public ProductDetailsPage submitReview(String name, String email, String text) {
        type(reviewName, name);
        type(reviewEmail, email);
        type(reviewText, text);
        click(reviewSubmit);
        return this;
    }

    public boolean reviewSuccessVisible() {
        try { return el(reviewSuccess).isDisplayed(); }
        catch (Exception e) { return false; }
    }
}
