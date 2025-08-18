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
- Scale: ≈37+ distinct test cases (TestNG @Test methods).
> Executions can exceed this count because some tests are data-driven (e.g., multiple payment cards) and some flows iterate products dynamically. 

---

## 🧭 Project structure

```text
webshop-selenium-testng/
├─ pom.xml
├─ .github/
│  └─ workflows/
│     └─ ci.yml                       # CI: smoke always; full if VALID_* secrets exist
├─ src/
│  ├─ main/
│  │  └─ java/com/MyStore/
│  │     ├─ core/
│  │     │  ├─ Config.java            # Reads baseUrl, headless, timeouts from system props
│  │     │  └─ DriverFactory.java     # WebDriver lifecycle (init/quit, single place)
│  │     ├─ data/
│  │     │  ├─ PaymentCard.java       # record(holder, number, cvc, month, year)
│  │     │  ├─ User.java              # POJO for registration data
│  │     │  └─ UserBuilder.java       # Builder to generate realistic User test data
│  │     └─ pages/                    # Page Objects (POM)
│  │        ├─ BasePage.java
│  │        ├─ HomePage.java
│  │        ├─ ProductsPage.java
│  │        ├─ ProductDetailsPage.java
│  │        ├─ CartPage.java
│  │        ├─ CheckoutPage.java
│  │        ├─ PaymentPage.java
│  │        ├─ OrderPlacedPage.java
│  │        ├─ AuthPage.java
│  │        └─ (others pages)
│  └─ test/
│     ├─ java/com/MyStore/
│     │  ├─ tests/
│     │  │  ├─ BaseTest.java          # Driver setup/teardown + listener hook
│     │  │  ├─ smoke/
│     │  │  │  ├─ products/
│     │  │  │  │  └─ ProductSmokeTest.java
│     │  │  │  ├─ checkout/
│     │  │  │  │  └─ CheckoutWithCardSmokeTest.java
│     │  │  │  ├─ auth/
│     │  │  │  │  ├─ AuthSmokeTest.java
│     │  │  │  │  └─ LogoutSmokeTest.java
│     │  │  │  ├─ home/
│     │  │  │  │  └─ HomeSmokeTest.java
│     │  │  │  └─ (other classes: nav, contact, etc.)
│     │  └─ data/
│     │     └─ PaymentCardProvider.java # TestNG @DataProvider with valid cards
│     └─ java/com/MyStore/tests/support/
│           └─ ScreenshotOnFailure.java # TestNG ITestListener → PNG on fail
└─ target/
   ├─ surefire-reports/                 # TestNG/Surefire HTML reports
   └─ screenshots/                      # Failure screenshots

```

## 🧱 Tech stack & utilities

- **Language:** Java 21  
- **Test framework:** TestNG 7.11  
- **Browser automation:** Selenium 4.33  
- **Build & runner:** Maven (Surefire)  
- **Data models:**
   - PaymentCard (record) + PaymentCardProvider (@DataProvider)
   - User + UserBuilder to generate fresh registration data
- **POM:** All user flows are modeled as Page Objects under  
  `src/main/java/com/MyStore/pages`  
- **Resilience:** `normalize-space()` XPaths, `scrollIntoView`, hover helpers, and explicit waits  
- **Screenshots:** `ScreenshotOnFailure` (`ITestListener`) saves PNGs to `target/screenshots`  

---

## ⚙️ Configuration

In `pom.xml`, these system properties can be overridden at runtime with `-D...` flags:

| Property         | Default                           | Description                            |
|------------------|-----------------------------------|----------------------------------------|
| `baseUrl`        | `https://automationexercise.com/` | Target site base URL                   |
| `headless`       | `false`                           | Run in headless mode (CI default = true) |
| `timeoutSeconds` | `10`                              | Max timeout in seconds for waits       |

**Credentials** (never hard-coded):

```bash
-DvalidEmail=<email>
-DvalidPassword=<password>
```

Some tests require login (e.g., add to cart after login, checkout with card).  
If no credentials are provided, those tests will gracefully skip or fail with a friendly message.

---

## 🚀 How to run

- 💡 PowerShell tip: to avoid Unknown lifecycle phase with -D…, either quote or use --%


### 1) Run everything (headless)
```bash
mvn -B -Dheadless=true clean test
```
### 2) Run only the smoke suite
```bash
mvn -B -Dheadless=true -Dgroups=smoke clean test
```
### 3) Smoke but exclude tests that need login
```bash
mvn -B -Dheadless=true -Dgroups="smoke and not auth" clean test
```
4) Full run with credentials
```bush
mvn test -Dheadless=true -DvalidEmail='test@AyaKhaled.com' -DvalidPassword='123456' -DvalidName='Aya'
```
5) Single class
```bush
mvn --% -Dtest=com.MyStore.tests.smoke.products.ProductSmokeTest test
```
6) Single method
```bush
mvn --% "-Dtest=com.MyStore.tests.smoke.products.ProductSmokeTest#searchFindsExpectedProduct" test
```

---

## 🖼️ Reports & screenshots

- HTML: target/surefire-reports/index.html
- PNGs on failure: target/screenshots/*.png

---

## ✅ Test coverage (examples)

| Area     | Test case                                           | Test class / method                                                  |
|----------|:---------------------------------------------------:|----------------------------------------------------------------------:|
| Products | Products page loads & shows tiles                   | `ProductSmokeTest.productsPageLoadsAndShowsTiles`                    |
| Products | Search returns “Blue Top”                           | `ProductSmokeTest.searchFindsExpectedProduct`                        |
| Auth     | Valid login lands on Home (“Logged in as…”)         | `AuthSmokeTest.validLoginGoesHome` *(needs creds)*                   |
| Cart     | Add first product and verify qty & totals           | `CartSmokeTest.addFirstProduct_verifyQtyAndTotals`                   |
| Checkout | Place order & download invoice                      | `CheckoutWithCardSmokeTest.canPlaceOrderAndDownloadInvoice` *(needs creds)* |
| Home UX  | Scroll down & back up (arrow), “Subscription” visible | `HomeSmokeTest.scrollDownThenScrollUpWithArrow`                    |
| …        | *(Other test cases not shown here: filters, reviews, invalid logins, etc.)* | —                             |

---

## 🧰 GitHub Actions (CI)

- **Workflow file:** `.github/workflows/ci.yml`

- **`smoke` job:**  
  Always runs on every push or pull request using:  
  ```bash
  -Dgroups="smoke and not auth"
  ```  
  Uploads test reports and screenshots as CI artifacts.

- **`full-with-creds` job:**  
  Runs only if the repository has these secrets set (and the build is not from a fork):  
  - `VALID_EMAIL`  
  - `VALID_PASSWORD`  
  These secrets are passed as system properties to Maven.

### 🔐 Set repository secrets

```
GitHub → Settings → Secrets and variables → Actions → New repository secret
```

Set the following:

- `VALID_EMAIL`
- `VALID_PASSWORD`

---

## 🛠️ Troubleshooting

- **PowerShell “Unknown lifecycle phase” error**  
  Use `--%` or quote the `-Dtest=…` value to prevent parsing issues:
  ```bash
  mvn --% "-Dtest=com.MyStore.tests.smoke.products.ProductSmokeTest#searchFindsExpectedProduct" test
  ```

- **Chrome CDP version warning**  
  When Chrome updates, you may see:  
  `"CDP version X not found; using Y"`  
  This is generally harmless. To silence the warning, update the Selenium version in `pom.xml`.

- **Missing credentials**  
  Any test involving authentication or checkout requires:  
  ```bash
  -DvalidEmail=<yourEmail> -DvalidPassword=<yourPassword>
  ```  
  If these aren't passed, the tests may be skipped or fail by design.  
  In CI, the smoke suite (without auth) runs by default.

---

## 📜 License

This project is licensed under the **MIT License**.  
See the [LICENSE](LICENSE) file for details.


