package com.MyStore.pages;

import com.MyStore.data.User;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class RegistrationPage extends BasePage {

    // --- Page identity ---
    private final By enterAccountInfo =
            By.xpath("//*[normalize-space()='Enter Account Information' or normalize-space()='ENTER ACCOUNT INFORMATION']");

    // --- Locators (only once, keep page lean) ---
    private final By genderMr        = By.id("id_gender1");
    private final By genderMrs       = By.id("id_gender2");
    private final By password        = By.id("password");
    private final By dayDropdown     = By.id("days");
    private final By monthDropdown   = By.id("months");
    private final By yearDropdown    = By.id("years");

    private final By newsletter      = By.id("newsletter");
    private final By optin           = By.id("optin");

    private final By firstName       = By.id("first_name");
    private final By lastName        = By.id("last_name");
    private final By company         = By.id("company");
    private final By address1        = By.id("address1");
    private final By address2        = By.id("address2");
    private final By countryDropdown = By.id("country");
    private final By state           = By.id("state");
    private final By city            = By.id("city");
    private final By zipcode         = By.id("zipcode");
    private final By mobileNumber    = By.id("mobile_number");

    private final By createAccountBtn = By.cssSelector("button[data-qa='create-account']");

    public enum Gender { MR, MRS }

    // --- Queries ---
    public boolean isLoaded() {
        try { return el(enterAccountInfo).isDisplayed(); }
        catch (Exception e) { return false; }
    }

    public RegistrationPage waitUntilLoaded() {
        el(enterAccountInfo);
        return this;
    }

    // --- tiny helpers (internal only) ---
    private void setCheckbox(By locator, boolean shouldBeChecked) {
        WebElement box = el(locator);
        if (box.isSelected() != shouldBeChecked)  click(locator);
    }

    private String[] splitName(String full) {
        if (full == null || full.isBlank()) return new String[]{"", ""};
        String[] parts = full.trim().split("\\s+", 2);
        return parts.length == 1 ? new String[]{parts[0], ""} : parts;
    }

    // --- Single, lean API for tests ---
    /** Fill the whole form from a User, with explicit gender + DOB. */
    public RegistrationPage fillFrom(User u, Gender gender,
                                     String day, String month, String year) {

        // Gender + account
        click(gender == Gender.MR ? genderMr : genderMrs);
        type(password, u.getPassword());
        new Select(el(dayDropdown)).selectByValue(day);
        new Select(el(monthDropdown)).selectByValue(month);
        new Select(el(yearDropdown)).selectByValue(year);
        setCheckbox(newsletter, true);
        setCheckbox(optin, true);

        // Names (prefer first/last; fallback to split full name)
        String fn = (u.getFirstName() != null && !u.getFirstName().isBlank())
                ? u.getFirstName() : splitName(u.getFullName())[0];
        String ln = (u.getLastName()  != null && !u.getLastName().isBlank())
                ? u.getLastName()  : splitName(u.getFullName())[1];

        // Address + contact
        type(firstName, fn);
        type(lastName, ln);
        type(company, u.getCompany());
        type(address1, u.getAddress1());
        type(address2, u.getAddress2());
        new Select(el(countryDropdown)).selectByVisibleText(u.getCountry());
        type(state, u.getState());
        type(city, u.getCity());
        type(zipcode, u.getZipcode());
        type(mobileNumber, u.getMobile());

        return this;
    }

    /** Convenience overload with sensible defaults so tests can just .fillFrom(user). */
    public RegistrationPage fillFrom(User u) {
        return fillFrom(u, Gender.MR, "10", "5", "1996");
    }

    public AccountCreatedPage submitCreateAccount() {
        click(createAccountBtn);
        return new AccountCreatedPage();
    }


    public RegistrationPage clearPassword() {
        type(password, ""); // BasePage.type() clears then sends ""
        return this;
    }

    public boolean passwordIsInvalid() {
        return !(Boolean)((org.openqa.selenium.JavascriptExecutor)driver)
                .executeScript("return arguments[0].checkValidity();", el(password));
    }

    public String passwordValidationMessage() {
        return (String)((org.openqa.selenium.JavascriptExecutor)driver)
                .executeScript("return arguments[0].validationMessage;", el(password));
    }

}
