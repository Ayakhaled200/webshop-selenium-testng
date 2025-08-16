package com.MyStore.tests.smoke.registration;


import com.MyStore.data.User;
import com.MyStore.data.UserBuilder;
import com.MyStore.pages.*;
import com.MyStore.tests.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class RegistrationSmokeTest extends BaseTest{

        @Test(groups = "smoke")
        //Happy Path
        public void canRegisterNewUser() {
            User u = new UserBuilder().build();

            RegistrationPage reg = new HomePage().goToAuth().startSignup(u);


            AccountCreatedPage done = reg
                    .fillFrom(u)               // or .fillFrom(u, Gender.MR, "10","5","1996")
                    .submitCreateAccount();

            Assert.assertTrue(done.isLoaded(), "Account Created page should be visible");

            HomePage home = done.continueToHome();
            Assert.assertTrue(home.isLoaded(), "Home should load after continue");
        }


    @Test(groups = {"smoke","negative"})
    //Try to sign up again with the same email → expect Auth page error
    public void duplicateEmailShowsErrorOnAuth() {
        User u = new UserBuilder().build();

        // 1) Create the account (happy path)
        HomePage home = new HomePage()
                .goToAuth()
                .startSignup(u)
                .waitUntilLoaded()
                .fillFrom(u)
                .submitCreateAccount()
                .continueToHome();
        Assert.assertTrue(home.isLoaded(), "Home should load after account created");

        // 2) Logout back to Auth
        AuthPage auth = home.logout();

        // 3) Try to sign up again with the same email → expect Auth page error
        auth.startSignup(u);
        Assert.assertTrue(auth.hasExistingEmailError(),
                "Expected 'Email Address already exist!' on Auth page");
    }


    @Test(groups = {"smoke","negative"})
    public void registrationPasswordRequired() {
        User u = new UserBuilder().build();

        RegistrationPage reg = new HomePage()
                .goToAuth()
                .startSignup(u)
                .waitUntilLoaded()
                .fillFrom(u)          // fills everything…
                .clearPassword();     // …then blanks password to trigger HTML5

        // Since HTML5 validation blocks form submission, we expect to still be on RegistrationPage
        Assert.assertTrue(reg.isLoaded(), "Should remain on Registration page when password is missing");
        Assert.assertTrue(reg.passwordIsInvalid(), "Password field should be invalid");
        Assert.assertTrue(reg.passwordValidationMessage().toLowerCase().contains("please fill out this field"),
                "Expected HTML5 message like 'Please fill out this field.'");
    }

}

