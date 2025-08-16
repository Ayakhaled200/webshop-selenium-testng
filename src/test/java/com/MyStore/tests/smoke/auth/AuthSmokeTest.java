package com.MyStore.tests.smoke.auth;

import com.MyStore.pages.AuthPage;
import com.MyStore.pages.HomePage;
import com.MyStore.pages.RegistrationPage;
import com.MyStore.tests.BaseTest;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static com.MyStore.pages.AuthPage.LoginOutcome.*; // for SUCCESS, SERVER_ERROR, etc.

public class AuthSmokeTest extends BaseTest {

    @Test(groups = {"auth", "smoke"})
    public void canOpenAuthFromHome() {
        var auth = new HomePage().goToAuth();
        Assert.assertTrue(auth.isSignupVisible(), "'New User Signup!' should be visible");
    }

    // Use the enum outcomes instead of checking DOM strings in tests
    @DataProvider(name = "loginCases")
    public Object[][] loginCases() {
        return new Object[][]{
                // email                                      , password   , expected outcome
                { "idontexist+" + System.currentTimeMillis() + "@example.com", "wrongpass", SERVER_ERROR },
                { ""                                          , ""          , EMAIL_REQUIRED },
                { "maybe@valid.com"                           , ""          , PASSWORD_REQUIRED },
                { ""                                          , "somepass"  , EMAIL_REQUIRED }
        };
    }

    @Test(dataProvider = "loginCases", groups = {"auth", "smoke"})
    public void loginOutcomes(String email, String password, AuthPage.LoginOutcome expected) {
        var auth = new HomePage().goToAuth();
        var actual = auth.tryLogin(email, password);
        Assert.assertEquals(actual, expected);
    }

    @Test(groups = {"auth", "smoke"})
    public void startSignupNavigatesToRegistration() {
        String name  = "Test User";
        String email = "auto" + System.currentTimeMillis() + "@testmail.com"; // unique
        RegistrationPage reg = new HomePage().goToAuth().startSignup(name, email);
        Assert.assertTrue(reg.isLoaded(), "Registration page should show 'Enter Account Information'");
    }

    @Test(groups = {"auth", "smoke"})
    public void validLoginGoesHome() {
        // Provide creds at runtime: -DvalidEmail=... -DvalidPassword=...
        String email = System.getProperty("validEmail");
        String pass  = System.getProperty("validPassword");
        if (email == null || pass == null) {
            throw new SkipException("Provide -DvalidEmail and -DvalidPassword to run this test");
        }

        var home = new HomePage().goToAuth().loginValid(email, pass);
        Assert.assertTrue(home.isLoaded(), "Home should load after valid login");
    }
}
