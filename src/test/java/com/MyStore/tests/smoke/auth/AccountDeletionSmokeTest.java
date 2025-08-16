package com.MyStore.tests.smoke.auth;

import com.MyStore.data.User;
import com.MyStore.data.UserBuilder;
import com.MyStore.pages.*;
import com.MyStore.tests.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class AccountDeletionSmokeTest extends BaseTest {

    @Test(groups = {"auth", "account", "smoke"})
    public void deleteFreshlyRegisteredAccount_returnsToHome() {
        // Register a disposable user so we can safely delete it
        User u = new UserBuilder().build();
        HomePage home = new HomePage().goToAuth()
                .startSignup(u)
                .fillFrom(u)
                .submitCreateAccount()
                .continueToHome();
        Assert.assertTrue(home.isLoaded(), "Home should load after registration");

        // Hit Delete Account (your Option B methods live in HomePage)
        home.startDeleteAccount();
        Assert.assertTrue(
                home.isAccountDeletedBannerVisible(),
                "'Account Deleted!' banner should be visible"
        );

        // Continue back to Home
        HomePage after = home.continueAfterAccountDeleted();
        Assert.assertTrue(after.isLoaded(), "Back on Home after clicking Continue");

        // prove we’re logged out by opening Auth (Login should be visible)
        Assert.assertTrue(
                after.goToAuth().isLoginVisible(),
                "Login should be visible after account deletion (user logged out)"
        );
    }
}
