package com.MyStore.tests.smoke.nav;

import com.MyStore.pages.*;
import com.MyStore.tests.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class NavSmokeTest extends BaseTest {

    // Products
    @Test(groups = {"nav","smoke"})
    public void header_opensProducts() {
        ProductsPage p = new HomePage().goToProducts();
        Assert.assertTrue(p.isLoaded(), "Products page should load");
        Assert.assertTrue(p.hasAnyProducts(), "Products list should not be empty");
    }

    // Cart
    @Test(groups = {"nav","smoke"})
    public void header_opensCart() {
        CartPage c = new HomePage().goToCart();
        Assert.assertTrue(c.isLoaded(), "Cart page should load (even if empty)");
    }

    // Test Cases
    @Test(groups = {"nav","smoke"})
    public void header_opensTestCases() {
        TestCasesPage t = new HomePage().goToTestCases();
        Assert.assertTrue(t.isLoaded(), "Test Cases header should be visible");
    }

    // Contact Us
    @Test(groups = {"nav","smoke"})
    public void header_opensContact() {
        ContactUsPage cu = new HomePage().goToContactUs();
        Assert.assertTrue(cu.isLoaded(), "Contact Us header should be visible");
    }
}
