package com.MyStore.tests.smoke.checkout;

import com.MyStore.data.User;
import com.MyStore.data.UserBuilder;
import com.MyStore.data.PaymentCard;
import com.MyStore.pages.*;
import com.MyStore.tests.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class GuestCheckoutRegistrationTest extends BaseTest {

    @Test(groups = {"checkout", "registration", "smoke"})
    public void guestIsAskedToLoginOrSignup_thenRegisters_andPlacesOrder() {
        // Add item while logged out
        ProductsPage products = new HomePage().goToProducts();
        CartPage cart = products.addFirstToCartAndGoToCart();
        Assert.assertTrue(cart.isLoaded());

        // Proceed → Auth
        AuthPage auth = cart.proceedToCheckoutExpectingAuth();
        Assert.assertTrue(auth.isSignupVisible(), "Auth page should be visible");

        // Register a new user
        User u = new UserBuilder().build();
        HomePage home = auth.startSignup(u)
                .fillFrom(u)
                .submitCreateAccount()
                .continueToHome();
        Assert.assertTrue(home.isLoaded(), "Home after registration");

        // Add another item and complete order
        ProductsPage products2 = home.goToProducts();
        CartPage cart2 = products2.addFirstToCartAndGoToCart();
        CheckoutPage checkout = cart2.proceedToCheckout();
        PaymentPage pay = checkout.placeOrder();     // <- no addComment

        PaymentCard card = new PaymentCard("Test Holder","4242424242424242","123","12","2030");
        OrderPlacedPage done = pay.pay(card);
        Assert.assertTrue(done.isLoaded());
        done.downloadInvoice().continueToHome();
    }
}
