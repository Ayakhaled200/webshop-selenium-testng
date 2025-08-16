package com.MyStore.tests.support;

import com.MyStore.core.DriverFactory;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotOnFailure implements ITestListener {
    @Override
    public void onTestFailure(ITestResult result) {
        try {
            var driver = DriverFactory.get();
            if (driver == null) return;

            byte[] png = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Path dir = Path.of("target", "screenshots");
            Files.createDirectories(dir);

            String name = result.getTestClass().getName() + "." +
                    result.getMethod().getMethodName() + "-" +
                    new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date()) +
                    ".png";
            Files.write(dir.resolve(name), png);
            System.out.println("Saved screenshot: target/screenshots/" + name);
        } catch (Exception ignored) {}
    }
}
