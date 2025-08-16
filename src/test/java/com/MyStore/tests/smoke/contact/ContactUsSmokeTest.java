package com.MyStore.tests.smoke.contact;

import com.MyStore.pages.ContactUsPage;
import com.MyStore.pages.HomePage;
import com.MyStore.tests.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.nio.file.Files;
import java.nio.file.Path;

public class ContactUsSmokeTest extends BaseTest {

    @Test(groups = {"contact","smoke"})
    public void contactUs_SubmitWithoutUpload_ShowsSuccess() {
        ContactUsPage c = new HomePage().goToContactUs();
        Assert.assertTrue(c.isLoaded(), "Contact Us header should be visible");

        c.fill("Aya QA", "auto+" + System.currentTimeMillis() + "@testmail.com",
                        "Hello", "Just testing the form.")
                .submit();

        Assert.assertTrue(c.successVisible(), "Success banner should appear after submit");
    }

    @Test(groups = {"contact","smoke"})
    public void contactUs_SubmitWithUpload_ShowsSuccess() throws Exception {
        ContactUsPage c = new HomePage().goToContactUs();
        Assert.assertTrue(c.isLoaded(), "Contact Us header should be visible");

        // Create a small temp file to upload
        Path tmp = Files.createTempFile("attachment-", ".txt");
        Files.writeString(tmp, "dummy content");

        c.fill("Aya QA", "auto+" + System.currentTimeMillis() + "@testmail.com",
                        "Hello with file", "Testing attachment.")
                .upload(tmp.toAbsolutePath().toString())
                .submit();

        Assert.assertTrue(c.successVisible(), "Success banner should appear after submit with upload");
    }
}
