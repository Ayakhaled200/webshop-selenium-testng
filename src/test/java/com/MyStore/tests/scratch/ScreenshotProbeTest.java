package com.MyStore.tests.scratch;

import com.MyStore.tests.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ScreenshotProbeTest extends BaseTest {

    @Test
    public void forceFailureToTestScreenshot() {
        // Do something trivial, then fail on purpose
        Assert.fail("Intentional failure to verify ScreenshotOnFailure");
    }
}
