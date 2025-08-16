// LogoutSmokeTest.java
package com.MyStore.tests.smoke.logout;

import com.MyStore.core.Config;
import com.MyStore.core.DriverFactory;
import com.MyStore.pages.AuthPage;
import com.MyStore.pages.HomePage;
import com.MyStore.tests.BaseTest;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;

public class LogoutSmokeTest extends BaseTest {

    @Test(groups = {"auth", "smoke"})
    public void canLogoutFromHome() {
        String email = System.getProperty("validEmail");
        String pass  = System.getProperty("validPassword");
        if (email == null || pass == null) {
            throw new SkipException("Provide -DvalidEmail and -DvalidPassword");
        }

        // ⬇️ Navigate straight to /login instead of clicking “Signup / Login”
        DriverFactory.get().get(Config.baseUrl() + "/login");
        AuthPage auth = new AuthPage();
        Assert.assertTrue(auth.isLoginVisible(), "Login page should be visible");

        // Login (your AuthPage.loginValid already waits for home)
        HomePage home = auth.loginValid(email, pass);

        // Logout and verify we’re back on Auth
        AuthPage after = home.logout();
        Assert.assertTrue(after.isLoginVisible(), "Login should be visible after logout");
    }
}
