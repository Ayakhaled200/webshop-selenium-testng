Webshop — Selenium + TestNG UI Tests

Automated UI test suite for automationexercise.com using Java 21, Selenium 4, and TestNG with the Page Object Model.
It covers key user journeys (browse products, search, cart, checkout, auth) and is wired for local runs and GitHub Actions CI (smoke always, full suite when repo secrets are present).

✨ Highlights

Java 21 / Selenium 4 / TestNG 7

Page Object Model with clean, stable locators & helper methods

Groups: smoke, products, checkout, auth, search, details, reviews, payment, address

Screenshots on failure (saved to target/screenshots)

Credentials passed via -DvalidEmail / -DvalidPassword (never hard-coded)

CI: GitHub Actions running smoke on every push; full suite when secrets exist

🧭 Project structure (path map)
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
│  │        └─ OrderPlacedPage.java
│  └─ test/
│     ├─ java/com/MyStore/
│     │  ├─ tests/
│     │  │  ├─ BaseTest.java        # Driver setup/teardown + listener hook
│     │  │  ├─ smoke/
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


Note: Class/package names above reflect the intended layout. If a file is missing in your repo, create it following the same pattern.

🧱 Tech stack & utilities

Language: Java 21

Test framework: TestNG 7.11

Browser automation: Selenium 4.33

Build & runner: Maven (Surefire)

Data: Java record PaymentCard, PaymentCardProvider with @DataProvider

POM: All user flows are modeled as Page Objects under src/main/java/com/MyStore/pages

Resilience: Normalize-space XPaths, scrollIntoView, hover helpers, and explicit waits

Screenshots: ScreenshotOnFailure (ITestListener) saves PNG to target/screenshots

⚙️ Configuration

pom.xml → surefire systemPropertyVariables (defaults; override with -D…):

baseUrl – default https://automationexercise.com/

headless – default false (true in CI/local optional)

timeoutSeconds – default 10

Credentials (never hard-coded):

-DvalidEmail=<email>

-DvalidPassword=<password>

Some tests require credentials (e.g., “add to cart after login”, “checkout with card”). Those tests auto-skip or handle missing creds gracefully.

🚀 How to run

PowerShell tip: to avoid Unknown lifecycle phase with -D…, either quote or use --%.

1) Run everything (headless)
mvn -B -Dheadless=true clean test

2) Run only the smoke suite
mvn -B -Dheadless=true -Dgroups=smoke clean test

3) Smoke but exclude tests that need login
mvn -B -Dheadless=true -Dgroups="smoke and not auth" clean test

4) Full run with credentials
mvn -B -Dheadless=true \
  -DvalidEmail="you@example.com" \
  -DvalidPassword="yourPassword" \
  clean test

5) Single class / single method
# class
mvn --% -Dtest=com.MyStore.tests.smoke.products.ProductSmokeTest test

# method
mvn --% "-Dtest=com.MyStore.tests.smoke.products.ProductSmokeTest#searchFindsExpectedProduct" test

Reports & screenshots

HTML: target/surefire-reports/index.html

PNGs on failure: target/screenshots/*.png

🧩 Page Objects (what each page does)

BasePage – common wait/click/type helpers, el(By) + small wrapper waits

HomePage – top-nav actions (goToProducts, goToAuth, etc.)

ProductsPage – search, filter (category/brand), add to cart (modal handling), open details
Locators are normalized for case, reduce flakiness, and avoid duplication.

ProductDetailsPage – read product meta (name, category, price, availability, condition, brand), set quantity, add to cart (to cart)

CartPage – verify lines & quantities, totals, proceed to checkout

CheckoutPage – address blocks (delivery/billing) text & navigation to payment

PaymentPage – pay with PaymentCard, submit order

OrderPlacedPage – success banner, download invoice, continue to home

✅ Test coverage (what & where)
Area	Test case	Pages used	Test class / method
Products	Products page loads & shows tiles	Home, Products	ProductSmokeTest.productsPageLoadsAndShowsTiles
	Search returns expected product (“Blue Top”)	Products	ProductSmokeTest.searchFindsExpectedProduct
	Open product details from listing	Products, ProductDetails	ProductSmokeTest.openProductDetailsFromListing
	Add to cart (stay on Products)	Products	ProductSmokeTest.addToCartFromListingAndStayOnProducts
	Add 2 products then go to cart & verify quantities	Products, Cart	ProductSmokeTest.addTwoProductsThenViewCartAndVerifyQuantities
	Set quantity on PDP and verify in cart	ProductDetails, Cart	ProductSmokeTest.setQuantityFromDetailsAndVerifyInCart
	Filter by Category: Women → Dress	Products	ProductSmokeTest.filterByCategory_Women_Dress_showsResults
	Filter by Brand: Polo	Products	ProductSmokeTest.filterByBrand_Polo_showsResults
	From brand results, add first item & go to cart	Products, Cart	ProductSmokeTest.addToCartAfterBrandFilter_thenGoToCart
	Submit review on PDP	Products, ProductDetails	ProductSmokeTest.canSubmitReviewOnProductDetails
Auth	Valid login lands on Home (header “Logged in as…”)	Home, Auth	AuthSmokeTest.validLoginGoesHome (needs creds)
	Logout from Home	Home	LogoutSmokeTest.canLogoutFromHome (needs creds)
Home UX	Scroll down & up via arrow; “Subscription” visible	Home	HomeSmokeTest.scrollDownThenScrollUpWithArrow
Cart	Add first product and verify qty & totals	Home, Products, Cart	CartSmokeTest.addFirstProduct_verifyQtyAndTotals
Checkout	Place order & download invoice	Products, Cart, Checkout, Payment, OrderPlaced	CheckoutWithCardSmokeTest.canPlaceOrderAndDownloadInvoice (needs creds + card)
	Checkout address blocks match newly registered user	Auth, Products, Cart, Checkout	CheckoutWithCardSmokeTest.checkoutAddressesMatchRegisteredUser

The suite uses groups so you can slice by feature quickly (e.g., -Dgroups=products, -Dgroups=checkout).

🔐 Credentials & data

Login: only passed as JVM properties (-DvalidEmail, -DvalidPassword).

Card: PaymentCard (record) + PaymentCardProvider supplies test cards to @Test(dataProvider=...).

🧪 Test design notes

Locators: prefer normalize-space() & case-insensitive matches for robust brand/category filters.

Modals: Add-to-cart modal is handled via Continue Shopping / View Cart helpers.

Scrolling & hover: scrollIntoView({block:'center'}) plus Actions.moveToElement to reveal hover controls.

Waits: centralised with el(By) (explicit wait); assertions check visibility/content, not brittle timing.

Listeners: ScreenshotOnFailure hooks into ITestListener to capture post-failure browser state.

🧰 GitHub Actions (CI)

Workflow: .github/workflows/ci.yml

Job check-creds: step reads secrets.VALID_EMAIL/PASSWORD and exposes have_creds output.

Job smoke: always runs (-Dgroups="smoke and not auth"). Artifacts: reports + screenshots.

Job full-with-creds: runs only if have_creds == true and not from external fork PR.
Passes secrets as env → -DvalidEmail/-DvalidPassword.

Set repo secrets:

Settings → Secrets and variables → Actions → New repository secret

VALID_EMAIL

VALID_PASSWORD

Artifacts uploaded:

target/surefire-reports/**

target/screenshots/**

🛠️ Troubleshooting

PowerShell “Unknown lifecycle phase”
Use --% or quote the whole -Dtest=… value:

mvn --% "-Dtest=com.MyStore.tests.smoke.products.ProductSmokeTest#searchFindsExpectedProduct" test


Chrome DevTools (CDP) version warning
If Chrome updates, you may see “CDP version X not found; using Y”.
Usually harmless; upgrade Selenium when available to quiet the warning.

Missing creds
Tests tagged auth / checkout will fail or skip without -DvalidEmail/-DvalidPassword.
In CI, only the smoke no-auth slice runs by default.

➕ Extending the suite

Create a Page Object under src/main/java/com/MyStore/pages (extend BasePage).

Add focused tests under src/test/java/com/MyStore/tests/... and tag with groups.

Use existing helpers for modal handling, hover, and navigation.

Keep locators resilient (normalize-space, case-insensitive when useful).

If your test needs login, read creds from system properties only.

📜 Example commands cheat-sheet
# Smoke (local, headless)
mvn -B -Dheadless=true -Dgroups=smoke clean test

# Smoke excluding auth slice
mvn -B -Dheadless=true -Dgroups="smoke and not auth" clean test

# Full suite with creds
mvn -B -Dheadless=true -DvalidEmail="you@example.com" -DvalidPassword="secret" clean test

# Single class
mvn --% -Dtest=com.MyStore.tests.smoke.checkout.CheckoutWithCardSmokeTest test

# Single method
mvn --% "-Dtest=com.MyStore.tests.smoke.products.ProductSmokeTest#filterByBrand_Polo_showsResults" test
