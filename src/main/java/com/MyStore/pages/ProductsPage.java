package com.MyStore.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

public class ProductsPage extends BasePage {

    // locators
    // Identity
    private final By productsHeader = By.xpath("//*[normalize-space()='All Products' or normalize-space()='ALL PRODUCTS']");
    // Search
    private final By searchInput = By.id("search_product");
    private final By searchBtn   = By.id("submit_search");

    private By productTileByName(String name) {
        return By.xpath("//div[contains(@class,'product-image-wrapper')][.//p[normalize-space()='" + name + "']]");
    }
    private By addToCartBtnUnderCard(String name) {
        return By.xpath("//p[normalize-space()='" + name + "']/following::a[normalize-space()='Add to cart'][1]");
    }
    private By viewProductLink(String name) {
        return By.xpath("//p[normalize-space()='" + name + "']/following::a[normalize-space()='View Product'][1]");
    }
    private final By productNameCells = By.cssSelector(".features_items .product-image-wrapper .productinfo p");

    private final By continueShoppingBtn = By.xpath("//button[normalize-space()='Continue Shopping']");
    private final By viewCartLinkInModal = By.xpath("//a[normalize-space()='View Cart']");

    // --- Locators: Category sidebar ---
    private final By categoriesHeader = By.xpath("//*[normalize-space()='Category' or normalize-space()='CATEGORIES']");
    private By categoryToggle(String main) {
        return By.xpath("//div[@id='accordian']//a[normalize-space()='" + main + "']");
    }
    private By subCategoryLink(String main, String sub) {
        return By.xpath("//div[@id='accordian']//a[normalize-space()='" + main + "']/following::a[normalize-space()='" + sub + "'][1]");
    }
    private By categoryHeaderText(String main, String sub) {
        // e.g., "Women - Dress Products"
        return By.xpath("//h2[contains(normalize-space(),'" + main + "') and contains(normalize-space(),'" + sub + "') and contains(normalize-space(),'Products')]");
    }

    // --- Locators: Brands sidebar ---
    private final By brandsHeader = By.xpath("//*[normalize-space()='Brands' or normalize-space()='BRANDS']");
    private By brandLink(String brand) {
        String b = brand.toLowerCase();
        return By.xpath(
                "//div[contains(@class,'brands_products')]//a[" +
                        "contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'" + b + "')" +
                        "]"
        );
    }

    private By brandHeaderText(String brand) {
        // e.g., "Brand - Polo Products"
        return By.xpath("//h2[contains(normalize-space(),'Brand') and contains(normalize-space(),'" + brand + "') and contains(normalize-space(),'Products')]");
    }

    // --- Locators: product tiles (for generic presence checks) ---
    private final By productTiles = By.cssSelector(".features_items .product-image-wrapper");


    // QUERIES
    public boolean isLoaded() { return el(productsHeader).isDisplayed(); }

    public boolean hasProduct(String name) {
        try { return el(productTileByName(name)).isDisplayed(); }
        catch (Exception e) { return false; }
    }

    public String firstProductName() {
        return driver.findElements(productNameCells).getFirst().getText().trim();
    }
    public ProductsPage addFirstToCart() {
        return addToCart(firstProductName());
    }
    public CartPage addFirstToCartAndGoToCart() {
        return addToCartAndGoToCart(firstProductName());
    }

    public boolean categoriesVisible() { return el(categoriesHeader).isDisplayed(); }
    public boolean brandsVisible()     { return el(brandsHeader).isDisplayed(); }
    public boolean hasAnyProducts()    { return !driver.findElements(productTiles).isEmpty(); }
    public boolean isCategoryApplied(String main, String sub) {
        try { return el(categoryHeaderText(main, sub)).isDisplayed(); } catch (Exception e) { return false; }
    }
    public boolean isBrandApplied(String brand) {
        try { return el(brandHeaderText(brand)).isDisplayed(); } catch (Exception e) { return false; }
    }


    // HELPERS (private)
    private void hoverTile(String name) {
        WebElement tile = el(productTileByName(name));
        ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({block:'center', inline:'nearest'});", tile);
        new Actions(driver).moveToElement(tile).perform();
    }


    // ACTIONS
    public ProductsPage search(String q) {
        type(searchInput, q);
        click(searchBtn);
        return this;
    }

    private ProductsPage waitModalAndContinue() {
        el(continueShoppingBtn);
        click(continueShoppingBtn);
        return this;
    }
    private CartPage waitModalAndGoToCart() {
        el(viewCartLinkInModal);
        click(viewCartLinkInModal);
        return new CartPage();
    }

    public ProductsPage addToCart(String productName) {
        // Hover if needed to reveal the button
        hoverTile(productName);
        click(addToCartBtnUnderCard(productName));
        // Wait modal then continue shopping
        el(continueShoppingBtn);
        click(continueShoppingBtn);
        return this;
    }

    public CartPage addToCartAndGoToCart(String productName) {
        hoverTile(productName);
        click(addToCartBtnUnderCard(productName));
        el(viewCartLinkInModal);
        click(viewCartLinkInModal);
        return new CartPage();
    }

    public ProductDetailsPage openDetails(String productName) {
        hoverTile(productName);
        click(viewProductLink(productName));
        return new ProductDetailsPage();
    }


    public ProductsPage selectCategory(String main, String sub) {
        click(categoryToggle(main));        // expand
        click(subCategoryLink(main, sub));  // choose subcategory
        return this;
    }

    public ProductsPage filterByBrand(String brand) {
        waitUntilLoaded();
        ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({block:'center', inline:'nearest'});", el(brandsHeader));
        var link = wait.until(org.openqa.selenium.support.ui.ExpectedConditions
                .presenceOfElementLocated(brandLink(brand)));
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", link);
        return this;
    }

    public ProductsPage waitUntilLoaded() { el(productsHeader); return this; }

}
