package com.MyStore.tests.smoke.home;

import com.MyStore.pages.CartPage;
import com.MyStore.pages.HomePage;
import com.MyStore.tests.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class HomeSmokeTest extends BaseTest {

    @Test(groups = {"home", "nav", "smoke"})
    public void homeLoadsAndNavigatesToAuth() {
        HomePage home = new HomePage();
        Assert.assertTrue(home.isLoaded(), "Home page should show the Brands sidebar");

        Assert.assertTrue(home.goToAuth().isSignupVisible(),
                "'New User Signup!' header should be visible on Auth page");
    }

    @Test(groups = {"home", "scroll", "smoke"})
    public void scrollDownThenScrollUpWithArrow() {
        HomePage home = new HomePage()
                .scrollToFooter()
                .clickScrollUpArrow();
        Assert.assertTrue(home.isAtTop(), "Should be back at the top after clicking Scroll Up arrow");
    }

    @Test(groups = {"home", "scroll", "smoke"})
    public void scrollDownThenScrollUpWithoutArrow() {
        HomePage home = new HomePage()
                .scrollToFooter()
                .scrollToTop();
        Assert.assertTrue(home.isAtTop(), "Should be at the top after JS scroll to 0");
    }

    @Test(groups = {"home", "subscription", "smoke"})
    public void subscribeFromHomeFooter() {
        HomePage home = new HomePage()
                .scrollToFooter()
                .subscribe("auto+" + System.currentTimeMillis() + "@testmail.com");
        Assert.assertTrue(home.subscriptionSuccessVisible(),
                "Home footer subscription success message");
    }

    @Test(groups = {"cart", "subscription", "smoke"})
    public void subscribeFromCartFooter() {
        CartPage cart = new HomePage()
                .scrollToFooter()
                .addFirstRecommendedToCart()
                .goToCartFromModal();
        cart.subscribe("auto+" + System.currentTimeMillis() + "@testmail.com");
        Assert.assertTrue(cart.subscriptionSuccessVisible(),
                "Cart footer subscription success message");
    }
}
