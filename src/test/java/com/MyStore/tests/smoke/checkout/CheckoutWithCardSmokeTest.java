package com.MyStore.tests.smoke.checkout;

import com.MyStore.data.PaymentCard;
import com.MyStore.data.PaymentCardProvider;
import com.MyStore.pages.*;
import com.MyStore.tests.BaseTest;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;
import com.MyStore.data.User;
import com.MyStore.data.UserBuilder;

public class CheckoutWithCardSmokeTest extends BaseTest {


    private HomePage loginOrSkip() {
        String email = System.getProperty("validEmail");
        String pass  = System.getProperty("validPassword");
        if (email == null || pass == null) {
            throw new SkipException("Provide -DvalidEmail and -DvalidPassword");
        }
        return new HomePage().goToAuth().loginValid(email, pass);
    }

    @Test(
            groups = {"checkout", "payment", "smoke"},
            dataProvider = "validCards",
            dataProviderClass = PaymentCardProvider.class
    )    public void canPlaceOrderAndDownloadInvoice(PaymentCard card) {
        // --- Arrange: ensure we're logged in (or skip if creds missing) ---
        HomePage home = loginOrSkip();                              // Helper reads -DvalidEmail/-DvalidPassword
        Assert.assertTrue(home.isLoaded(), "Home should load after login");

        // --- Act: add an item to cart from Products listing ---
        ProductsPage products = home.goToProducts();                // Navigate to /products
        CartPage cart = products.addFirstToCartAndGoToCart();       // Add first visible product → open Cart modal → View Cart
        Assert.assertTrue(cart.isLoaded(), "Cart should load");

        // --- Proceed to Checkout (Address + Review step) ---
        CheckoutPage checkout = cart.proceedToCheckout();           // Click "Proceed To Checkout"
        Assert.assertTrue(checkout.isLoaded(), "Checkout should show Address/Review");

        // --- Place Order → go to Payment page ---
        PaymentPage pay = checkout.placeOrder();                    // Click "Place Order" (no comment/notes step on this flow)
        Assert.assertTrue(pay.isLoaded(), "Payment page should load");

        // --- Pay using the provided PaymentCard from the data provider ---
        OrderPlacedPage done = pay.pay(card);                       // Fill card fields and submit payment
        Assert.assertTrue(done.isLoaded(), "Order placed banner should be visible");

        // --- Post-conditions: download invoice and return Home ---
        done.downloadInvoice()                                      // Click “Download Invoice”
                .continueToHome();                                      // Click “Continue”
        Assert.assertTrue(new HomePage().isLoaded(), "Back to Home after continue");
    }

    @Test(groups = {"checkout", "address", "smoke"})
    public void checkoutAddressesMatchRegisteredUser() {
        // 1) Register a brand-new user so we know the expected address
        User u = new UserBuilder().build();

        HomePage home = new HomePage().goToAuth()
                .startSignup(u)
                .fillFrom(u)
                .submitCreateAccount()
                .continueToHome();
        Assert.assertTrue(home.isLoaded(), "Home should load after registration");

        // 2) Add any product and go to checkout
        ProductsPage products = home.goToProducts();
        CartPage cart = products.addFirstToCartAndGoToCart();
        Assert.assertTrue(cart.isLoaded(), "Cart should load");

        CheckoutPage checkout = cart.proceedToCheckout();
        Assert.assertTrue(checkout.isLoaded(), "Checkout should show Address/Review");

        // 3) Verify delivery/billing address blocks contain user data
        String delivery = checkout.deliveryAddressText().toLowerCase();
        String billing  = checkout.billingAddressText().toLowerCase();

        // Name: accept either first/last or full name depending on site formatting
        Assert.assertTrue(
                delivery.contains(u.getFirstName().toLowerCase())
                        || delivery.contains(u.getFullName().toLowerCase()),
                "Delivery name should contain user's name"
        );

        // Address lines (keeping assertions 'contains' and case-insensitive for robustness)
        Assert.assertTrue(delivery.contains(u.getAddress1().toLowerCase()), "Delivery should show Address1");
        Assert.assertTrue(delivery.contains(u.getCity().toLowerCase()),     "Delivery should show City");
        Assert.assertTrue(delivery.contains(u.getCountry().toLowerCase()),  "Delivery should show Country");

        // Often billing == delivery on this site; at least ensure it’s present
        Assert.assertFalse(billing.isBlank(), "Billing address block should be present");
    }
}
