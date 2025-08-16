package com.MyStore.tests.smoke.products;

import com.MyStore.pages.*;
        import com.MyStore.tests.BaseTest;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;

public class ProductSmokeTest  extends BaseTest {

    private ProductsPage openProducts() {
        return new HomePage().goToProducts();
    }

    private HomePage loginOrSkip() {
        String email = System.getProperty("validEmail");
        String pass  = System.getProperty("validPassword");
        if (email == null || pass == null) throw new SkipException("Provide -DvalidEmail -DvalidPassword");
        return new HomePage().goToAuth().loginValid(email, pass);
    }

    @Test(groups = {"products", "smoke"})
    public void productsPageLoadsAndShowsTiles() {
        ProductsPage products = openProducts();
        Assert.assertTrue(products.isLoaded(), "Products page should load");
        Assert.assertTrue(products.hasAnyProducts(), "Products list should not be empty");
    }

    @Test(groups = {"products", "smoke"})
    public void searchFindsExpectedProduct() {
        ProductsPage products = openProducts();
        products.search("Top");
        Assert.assertTrue(products.hasProduct("Blue Top"), "Search results should contain 'Blue Top'");
    }

    @Test(groups = {"products", "smoke"})
    public void openProductDetailsFromListing() {
        ProductsPage products = openProducts();
        ProductDetailsPage details = products.openDetails("Blue Top");
        Assert.assertTrue(details.isLoaded(), "Product details page should load");
    }

    @Test(groups = {"products", "smoke"})
    public void addToCartFromListingAndStayOnProducts() {
        ProductsPage products = openProducts();
        products.addToCart("Blue Top");
        Assert.assertTrue(products.isLoaded(), "Should remain on Products page after Continue Shopping");
        products.addToCart("Men Tshirt");
        Assert.assertTrue(products.isLoaded(), "Still on Products page after second add");
    }

    @Test(groups = {"products", "smoke"})
    public void addTwoProductsThenViewCartAndVerifyQuantities() {
        ProductsPage products = openProducts();
        products.addToCart("Blue Top");
        CartPage cart = products.addToCartAndGoToCart("Men Tshirt");
        Assert.assertTrue(cart.isLoaded(), "Cart page should load");
        Assert.assertEquals(cart.getQuantity("Blue Top"), 1, "Blue Top qty should be 1");
        Assert.assertEquals(cart.getQuantity("Men Tshirt"), 1, "Men Tshirt qty should be 1");
    }

    @Test(groups = {"products", "smoke"})
    public void setQuantityFromDetailsAndVerifyInCart() {
        ProductsPage products = openProducts();
        ProductDetailsPage details = products.openDetails("Blue Top");
        Assert.assertTrue(details.isLoaded(), "Details page should load");
        CartPage cart = details.setQuantity(3).addToCartGoToCart();
        Assert.assertTrue(cart.isLoaded(), "Cart should load");
        Assert.assertEquals(cart.getQuantity("Blue Top"), 3, "Quantity should be 3 after setting on details page");
    }

    @Test(groups = {"products", "smoke"})
    public void filterByCategory_Women_Dress_showsResults() {
        ProductsPage products = openProducts();
        Assert.assertTrue(products.categoriesVisible(), "Category sidebar should be visible");
        products.selectCategory("Women", "Dress");
        Assert.assertTrue(products.isCategoryApplied("Women", "Dress"), "Header should reflect Women - Dress");
        Assert.assertTrue(products.hasAnyProducts(), "Category filter should list products");
    }

    @Test(groups = {"products", "smoke"})
    public void filterByBrand_Polo_showsResults() {
        ProductsPage products = openProducts();
        Assert.assertTrue(products.brandsVisible(), "Brands sidebar should be visible");
        products.filterByBrand("Polo");
        Assert.assertTrue(products.isBrandApplied("Polo"), "Header should reflect Brand - Polo");
        Assert.assertTrue(products.hasAnyProducts(), "Brand filter should list products");
    }

    @Test(groups = {"products" , "smoke"}, dependsOnMethods = "filterByBrand_Polo_showsResults")
    public void addToCartAfterBrandFilter_thenGoToCart() {
        ProductsPage products = openProducts();
        products.filterByBrand("Polo");
        Assert.assertTrue(products.isBrandApplied("Polo"));
        // Prefer using the helper; if you didn't add it, replace with a concrete product name under "Polo".
        CartPage cart = products.addFirstToCartAndGoToCart();
        Assert.assertTrue(cart.isLoaded(), "Cart should load after adding from brand-filtered results");
        // We can't assert the exact name without a known product; presence of at least 1 item is enough here.
    }


    @Test(groups = {"products", "search", "smoke"})
    public void searchThenAddToCartAfterLogin() {
        HomePage home = loginOrSkip();
        ProductsPage products = home.goToProducts().search("dress");
        Assert.assertTrue(products.hasAnyProducts(), "Search should return results");

        String chosen = products.firstProductName();
        CartPage cart = products.addFirstToCartAndGoToCart();
        Assert.assertTrue(cart.hasProduct(chosen), "Cart should contain the searched product: " + chosen);
    }

    @Test(groups = {"products", "details", "smoke"})
    public void detailsShowAllKeyFields() {
        ProductsPage products = new HomePage().goToProducts();
        String name = products.firstProductName();
        ProductDetailsPage pdp = products.openDetails(name);
        Assert.assertTrue(pdp.isLoaded(), "Details page should load");

        Assert.assertEquals(pdp.productName(), name, "Name on PDP should match clicked product");
        Assert.assertFalse(pdp.productCategory().isBlank(), "Category should be present");
        Assert.assertFalse(pdp.productPrice().isBlank(), "Price should be present");
        Assert.assertFalse(pdp.productAvailability().isBlank(), "Availability should be present");
        Assert.assertFalse(pdp.productCondition().isBlank(), "Condition should be present");
        Assert.assertFalse(pdp.productBrand().isBlank(), "Brand should be present");
    }

    @Test(groups = {"products", "reviews", "smoke"})
    public void canSubmitReviewOnProductDetails() {
        ProductsPage products = new HomePage().goToProducts();
        String productName = products.firstProductName();

        ProductDetailsPage pdp = products.openDetails(productName);
        Assert.assertTrue(pdp.isLoaded(), "PDP should load");

        String email = "auto+" + System.currentTimeMillis() + "@testmail.com";
        pdp.submitReview("Auto QA", email, "Nice product, adding an automated review.");

        Assert.assertTrue(pdp.reviewSuccessVisible(), "PDP should show 'Thank you for your review.'");
    }


}
