# 🛒 Webshop — Selenium + TestNG UI Tests

Automated UI test suite for [automationexercise.com](https://automationexercise.com) using **Java 21**, **Selenium 4**, and **TestNG** with the **Page Object Model**.  
It covers key user journeys (browse products, search, cart, checkout, auth) and is wired for local runs and GitHub Actions CI (smoke always, full suite when repo secrets are present).

---

## ✨ Highlights

- Java 21 / Selenium 4 / TestNG 7  
- Page Object Model with clean, stable locators & helper methods  
- Test groups: `smoke`, `products`, `checkout`, `auth`, `search`, `details`, `reviews`, `payment`, `address`  
- Screenshots on failure (saved to `target/screenshots`)  
- Credentials passed via `-DvalidEmail` / `-DvalidPassword` (never hard-coded)  
- CI: GitHub Actions running smoke on every push; full suite when secrets exist  

---

## 🧭 Project structure

```text
webshop-selenium-testng/
├─ pom.xml
├─ .github/
│  └─ workflows/
│     └─ ci.yml                     # CI: smoke always, full if VALID_* secrets exist
├─ src/
│  ├─ main/
│  │  └─ java/com/MyStore/
│  │     ├─ core/
│  │     │  ├─ Config.java          # Reads baseUrl, headless, timeouts from system props
│  │     │  └─ DriverFactory.java   # WebDriver lifecycle (init/quit, single place)
│  │     ├─ data/
│  │     │  └─ PaymentCard.java     # record(holder, number, cvc, month, year)
│  │     └─ pages/                  # Page Objects (POM)
│  │        ├─ BasePage.java
│  │        ├─ HomePage.java
│  │        ├─ ProductsPage.java
│  │        ├─ ProductDetailsPage.java
│  │        ├─ CartPage.java
│  │        ├─ CheckoutPage.java
│  │        ├─ PaymentPage.java
│  │        ├─ OrderPlacedPage.java
│  │        └─ ... (other Page Objects)
│  └─ test/
│     ├─ java/com/MyStore/
│     │  ├─ tests/
│     │  │  ├─ BaseTest.java        # Driver setup/teardown + listener hook
│     │  │  ├─ smoke/               # Example test group (other groups exist too)
│     │  │  │  ├─ products/
│     │  │  │  │  └─ ProductSmokeTest.java
│     │  │  │  ├─ checkout/
│     │  │  │  │  └─ CheckoutWithCardSmokeTest.java
│     │  │  │  ├─ auth/
│     │  │  │  │  ├─ AuthSmokeTest.java
│     │  │  │  │  └─ LogoutSmokeTest.java
│     │  │  │  └─ home/
│     │  │  │     └─ HomeSmokeTest.java
│     │  └─ data/
│     │     └─ PaymentCardProvider.java   # TestNG @DataProvider with valid cards
│     └─ java/com/MyStore/tests/support/
│           └─ ScreenshotOnFailure.java   # TestNG ITestListener → PNG on fail
└─ target/
   ├─ surefire-reports/                   # TestNG/Surefire HTML reports
   └─ screenshots/                        # Failure screenshots

---

🧱 Tech stack & utilities

Language: Java 21

Test framework: TestNG 7.11

Browser automation: Selenium 4.33

Build & runner: Maven (Surefire)

Data: PaymentCard (Java record) + PaymentCardProvider with @DataProvider

POM: All user flows are modeled as Page Objects under src/main/java/com/MyStore/pages

Resilience: normalize-space() XPaths, scrollIntoView, hover helpers, and explicit waits

Screenshots: ScreenshotOnFailure (ITestListener) saves PNGs to target/screenshots
