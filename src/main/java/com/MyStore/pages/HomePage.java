package com.MyStore.pages;

import com.MyStore.core.Config;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.ElementClickInterceptedException;

public class HomePage extends BasePage {

    // Header / nav
    private final By brandsSidebar = By.cssSelector(".brands_products");
    private final By signupLogin   = By.linkText("Signup / Login");
    private final By loggedInAs    = By.xpath("//a[contains(.,'Logged in as')]");
    private final By logoutLink    = By.cssSelector("a[href='/logout']");
    private final By productsLink  = By.cssSelector("a[href='/products']");
    private final By testCasesLink = By.cssSelector("a[href='/test_cases']");
    private final By cartLink = By.cssSelector("a[href='/view_cart']");

    // Footer subscription
    private final By subscribeEmail   = By.id("susbscribe_email"); // (site typo)
    private final By subscribeButton  = By.id("subscribe");
    private final By subscribeSuccess = By.xpath("//*[contains(normalize-space(),'You have been successfully subscribed')]");

    // Recommended block
    private final By recommendedBlock           = By.id("recommended-item-carousel");
    private final By firstRecommendedAddToCart  =
            By.xpath("(//div[@id='recommended-item-carousel']//a[normalize-space()='Add to cart'])[1]");
    private final By viewCartLinkInModal =
            By.xpath("//u[normalize-space()='View Cart']/.. | //a[normalize-space()='View Cart']");

    // Scroll up
    private final By footerSubscriptionHeader =
            By.xpath("//*[normalize-space()='Subscription' or normalize-space()='SUBSCRIPTION']");
    private final By scrollUpArrow = By.id("scrollUp");

    // Contact Us
    private final By contactUsLink = By.cssSelector("a[href='/contact_us']");

    // Delete Account
    private final By deleteAccountLink     = By.cssSelector("a[href='/delete_account']");
    private final By accountDeletedBanner  = By.xpath("//*[normalize-space()='Account Deleted!' or normalize-space()='ACCOUNT DELETED!']");
    private final By accountDeletedContinue= By.xpath("//a[normalize-space()='Continue']");

    // ---------- Queries ----------
    public boolean isLoaded() { return el(brandsSidebar).isDisplayed(); }

    // ---------- Navigation ----------
    public AuthPage goToAuth() {
        try {
            // try the header link first
            click(signupLogin);
        } catch (ElementClickInterceptedException | TimeoutException e) {
            // fallback: navigate directly
            driver.get(Config.baseUrl() + "/login");
        }
        // tiny sanity wait: the login email is present on /login
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("input[data-qa='login-email']")));
        return new AuthPage();
    }

    public ProductsPage goToProducts() {
        try {
            // try the header link first
            click(productsLink);
        } catch (ElementClickInterceptedException | TimeoutException e) {
            // fallback: navigate directly
            driver.get(Config.baseUrl() + "/products");
        }
        // wait for Products identity (your page object checks this)
        return new ProductsPage().waitUntilLoaded();
    }

    public TestCasesPage goToTestCases() {
        click(testCasesLink);
        return new TestCasesPage();
    }

    // ---------- Auth helpers ----------
    public HomePage waitUntilLoggedIn() { el(loggedInAs); return this; }

    public boolean isLoggedInAs(String name) {
        String text = el(loggedInAs).getText().trim();
        return text.equalsIgnoreCase("Logged in as " + name);
    }

    public AuthPage logout() {
        click(logoutLink);
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h2[normalize-space()='Login to your account']")));
        return new AuthPage();
    }

    // ---------- Footer subscription ----------
    public HomePage subscribe(String email) {
        scrollToFooter();
        type(subscribeEmail, email);
        click(subscribeButton);
        return this;
    }

    public boolean subscriptionSuccessVisible() {
        try { return el(subscribeSuccess).isDisplayed(); }
        catch (Exception ignored) { return false; }
    }

    // ---------- Recommended items ----------
    public HomePage addFirstRecommendedToCart() {
        scrollToFooter();
        // Ensure the block is visible
        wait.until(ExpectedConditions.visibilityOfElementLocated(recommendedBlock));

        // Try normal click first
        click(firstRecommendedAddToCart);
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(viewCartLinkInModal));
            return this;
        } catch (TimeoutException ignored) {
            // Fallback to JS click (sometimes the link is under a slick carousel overlay)
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el(firstRecommendedAddToCart));
            wait.until(ExpectedConditions.visibilityOfElementLocated(viewCartLinkInModal));
            return this;
        }
    }

    // Cart
    public CartPage goToCart() {
        click(cartLink);
        return new CartPage();
    }
    public CartPage goToCartFromModal() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(viewCartLinkInModal));
        click(viewCartLinkInModal);
        return new CartPage();
    }

    // ---------- Scroll helpers ----------
    public HomePage scrollToFooter() {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
        wait.until(ExpectedConditions.visibilityOfElementLocated(footerSubscriptionHeader));
        return this;
    }

    public HomePage clickScrollUpArrow() {
        // Make sure the arrow is present then click and wait for top
        el(scrollUpArrow);
        click(scrollUpArrow);
        wait.until(d -> isAtTop());
        return this;
    }

    public HomePage scrollToTop() {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0,0)");
        wait.until(d -> isAtTop());
        return this;
    }

    public boolean isAtTop() {
        Long y = (Long) ((JavascriptExecutor) driver)
                .executeScript("return Math.round(window.pageYOffset||document.documentElement.scrollTop||document.body.scrollTop||0);");
        return y <= 5;
    }

    public ContactUsPage goToContactUs() {
        click(contactUsLink);
        return new ContactUsPage();
    }

    // delete and come back to Home
    // trigger deletion (navigates away from Home)
    public HomePage startDeleteAccount() {
        click(deleteAccountLink);
        return this; // the *object* is still HomePage, but the browser is on the deleted screen
    }

    // assertion helper for the deleted screen
    public boolean isAccountDeletedBannerVisible() {
        try { return el(accountDeletedBanner).isDisplayed(); }
        catch (Exception e) { return false; }
    }

    // continue back to Home
    public HomePage continueAfterAccountDeleted() {
        click(accountDeletedContinue);
        return new HomePage();
    }
}
