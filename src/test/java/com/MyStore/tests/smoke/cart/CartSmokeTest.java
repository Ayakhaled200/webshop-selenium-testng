package com.MyStore.tests.smoke.cart;

import com.MyStore.pages.*;
import com.MyStore.tests.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CartSmokeTest extends BaseTest {

    // Helper: add the first product from listing and go to cart
    private CartPage addFirstAndOpenCart(ProductsPage products) {
        return products.addFirstToCartAndGoToCart();
    }

    @Test(groups = {"cart", "smoke"})
    public void addFirstProduct_verifyQtyAndTotals() {
        ProductsPage products = new HomePage().goToProducts();
        String name = products.firstProductName();

        CartPage cart = addFirstAndOpenCart(products);
        Assert.assertTrue(cart.isLoaded(), "Cart page should load");
        Assert.assertTrue(cart.hasProduct(name), "Cart should contain the product just added");

        int qty   = cart.quantityOf(name);
        int unit  = cart.unitPriceOf(name);
        int total = cart.lineTotalOf(name);
        Assert.assertTrue(qty >= 1, "Quantity should be >= 1");
        Assert.assertEquals(total, qty * unit, "Line total = qty * unit price");
    }

    @Test(groups = {"cart", "smoke"})
    public void removeLastItem_makesCartEmpty() {
        ProductsPage products = new HomePage().goToProducts();
        String name = products.firstProductName();

        CartPage cart = addFirstAndOpenCart(products);
        Assert.assertTrue(cart.hasProduct(name), "Precondition: product present in cart");

        cart.removeAndWait(name);
        Assert.assertTrue(cart.isEmpty(), "Cart should be empty after removing the only item");
    }

    @Test(groups = {"cart", "checkout", "smoke"})
    public void proceedToCheckout_anonymous_leadsToAuth() {
        ProductsPage products = new HomePage().goToProducts();
        CartPage cart = addFirstAndOpenCart(products);

        AuthPage auth = cart.proceedToCheckoutExpectingAuth();
        Assert.assertTrue(auth.isLoginVisible(), "Login header should be visible after checkout as anonymous");
    }

    @Test(groups = {"cart", "subscription", "smoke"})
    public void subscriptionOnCart_showsSuccess() {
        ProductsPage products = new HomePage().goToProducts();
        CartPage cart = addFirstAndOpenCart(products);

        String email = "auto+" + System.currentTimeMillis() + "@testmail.com";
        cart.subscribe(email);
        Assert.assertTrue(cart.subscriptionSuccess(), "Subscription success banner should appear on cart page");
    }
}
