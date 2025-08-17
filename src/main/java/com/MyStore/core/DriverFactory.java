package com.MyStore.core;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;

public final class DriverFactory {
    // One driver per thread; avoids clashes in parallel execution
    private static final ThreadLocal<WebDriver> TL = new ThreadLocal<>();

    public static boolean isStarted() {
        return TL.get() != null; // or whatever ThreadLocal/WebDriver holder you use
    }
    // Create the driver once per test (idempotent)
    public static void init(boolean headless) {
        if (TL.get() != null) return;

        ChromeOptions opts = new ChromeOptions();
        if (headless) opts.addArguments("--headless=new");
        opts.addArguments("--window-size=1920,1080", "--disable-gpu");

        ChromeDriver driver = new ChromeDriver(opts); // Selenium Manager auto-downloads the driver
        // We use EXPLICIT waits everywhere → keep implicit wait at 0
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(30));

        TL.set(driver);
    }

    // Get the current thread's driver, or fail fast if not initialized
    public static WebDriver get() {
        WebDriver d = TL.get();
        if (d == null) throw new IllegalStateException("Driver not initialized. Call DriverFactory.init()");
        return d;
    }

    // Quit and clean the ThreadLocal to prevent memory leaks
    public static void quit() {
        WebDriver d = TL.get();
        if (d != null) { d.quit(); TL.remove(); }
    }

    private DriverFactory() {}
}
