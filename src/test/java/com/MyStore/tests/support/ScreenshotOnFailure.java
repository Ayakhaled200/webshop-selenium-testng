package com.MyStore.tests.support;

import com.MyStore.core.DriverFactory;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScreenshotOnFailure implements ITestListener {

    private static final DateTimeFormatter TS =
            DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");

    @Override
    public void onTestFailure(ITestResult result) {
        WebDriver driver;
        try {
            // If driver isn't started, just skip (don't throw)
            driver = DriverFactory.get();
        } catch (Throwable noDriver) {
            System.out.println("[ScreenshotOnFailure] No driver; skipping screenshot");
            return;
        }

        try {
            if (!(driver instanceof TakesScreenshot ts)) {
                System.out.println("[ScreenshotOnFailure] Driver can't take screenshots");
                return;
            }

            File src = ts.getScreenshotAs(OutputType.FILE);

            Path dir = Paths.get("target", "screenshots");
            Files.createDirectories(dir);

            String base =
                    result.getTestClass().getName() + "." + result.getName() + "_" + TS.format(LocalDateTime.now());
            String safe = base.replaceAll("[^a-zA-Z0-9._-]", "_") + ".png";

            Path dest = dir.resolve(safe);
            Files.copy(src.toPath(), dest, StandardCopyOption.REPLACE_EXISTING);

            System.out.println("[ScreenshotOnFailure] Saved -> " + dest.toAbsolutePath());
        } catch (Throwable t) {
            // Never fail the run from the listener
            System.out.println("[ScreenshotOnFailure] Suppressed while capturing: " + t);
        }
    }
}
