package com.MyStore.tests;

import org.testng.annotations.Listeners;
import com.MyStore.core.Config;
import com.MyStore.core.DriverFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

@Listeners({ com.MyStore.tests.support.ScreenshotOnFailure.class })
public abstract class BaseTest {

    @BeforeMethod
    public void setUp() {
        DriverFactory.init(Config.headless());
        DriverFactory.get().get(Config.baseUrl());
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        DriverFactory.quit();
    }
}
