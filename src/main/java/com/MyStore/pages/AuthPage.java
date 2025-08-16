package com.MyStore.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.MyStore.data.User;

import java.time.Duration;

public class AuthPage extends BasePage {

    // --- Outcome of a login attempt (what your test will assert) ---
    public enum LoginOutcome { SUCCESS, SERVER_ERROR, EMAIL_REQUIRED, PASSWORD_REQUIRED }

    // --- Locators ---
    // Page identity
    private final By newUserHeader = By.xpath("//h2[normalize-space()='New User Signup!']");

    // --- Signup ---
    private final By signupName    = By.name("name");
    private final By signupEmail   = By.cssSelector("input[data-qa='signup-email']");
    private final By signupBtn     = By.xpath("//button[normalize-space()='Signup']");

    // --- Login ---
    private final By loginEmail     = By.cssSelector("input[data-qa='login-email']");
    private final By loginPassword  = By.cssSelector("input[data-qa='login-password']");
    private final By loginBtn       = By.xpath("//button[normalize-space()='Login']");
    private final By loginErrorText = By.xpath("//p[normalize-space()=\"Your email or password is incorrect!\"]");


    private final By existingEmailError =
            By.xpath("//*[contains(normalize-space(),'Email Address already exist')]");
    private final By loginHeader = By.xpath("//h2[normalize-space()='Login to your account']");

    // Queries
    public boolean isSignupVisible() { return el(newUserHeader).isDisplayed(); }
    public boolean isLoginVisible() { return el(loginHeader).isDisplayed(); }


    public boolean hasExistingEmailError() {
        try { return el(existingEmailError).isDisplayed(); }
        catch (Exception e) { return false; }
    }

    // Actions
    // --- One method to try login and tell us what happened ---
    public LoginOutcome tryLogin(String email, String password) {
        // type + click
        type(loginEmail, email);
        type(loginPassword, password);
        click(loginBtn);

        // HTML5 validation (blank/invalid fields) – checked via JS on the inputs
        JavascriptExecutor js = (JavascriptExecutor) driver;
        boolean emailValid = (Boolean) js.executeScript("return arguments[0].checkValidity();", el(loginEmail));
        boolean passValid = (Boolean) js.executeScript("return arguments[0].checkValidity();", el(loginPassword));
        if (!emailValid) return LoginOutcome.EMAIL_REQUIRED;
        if (!passValid) return LoginOutcome.PASSWORD_REQUIRED;

        // Server-side banner (wrong but filled)
        try {
            new WebDriverWait(driver, Duration.ofSeconds(3))
                    .until(ExpectedConditions.visibilityOfElementLocated(loginErrorText));
            return LoginOutcome.SERVER_ERROR;
        } catch (TimeoutException ignored) {
            // No server error appeared quickly → treat as success.
            // (Optional: make this stricter by waiting for "Logged in as" on Home.)
            return LoginOutcome.SUCCESS;
        }
    }

    public RegistrationPage startSignup(String name, String email) {
        type(signupName, name);
        type(signupEmail, email);
        click(signupBtn);
        return new RegistrationPage();
    }

    public RegistrationPage startSignup(User user) {
        String displayName =
                (user.getFullName() != null && !user.getFullName().isBlank())
                        ? user.getFullName()
                        : (user.getFirstName() + " " + user.getLastName()).trim();

        return startSignup(displayName, user.getEmail()); // calls your existing String,String method
    }

    // Valid login → you land on Home → return HomePage
    public HomePage loginValid(String email, String password) {
        type(loginEmail, email);
        type(loginPassword, password);
        click(loginBtn);
        return new HomePage().waitUntilLoggedIn(); // robust: wait for the logged-in banner
    }

    public AuthPage loginInvalid(String email, String password) {
        type(loginEmail, email);
        type(loginPassword, password);
        click(loginBtn);
        return this; // stays on Auth page when invalid
    }
}
