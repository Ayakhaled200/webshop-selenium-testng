package com.MyStore.data;

import org.testng.annotations.DataProvider;

public class PaymentCardProvider {

    @DataProvider(name = "validCards")
    public Object[][] validCards() {
        return new Object[][]{
                { new PaymentCard("Test Holder", "4242424242424242", "123", "12", "2030") },
                { new PaymentCard("Test Holder", "5555555555554444", "321", "11", "2031") }
        };
    }

    // (HTML5/invalid scenarios)
    @DataProvider(name = "invalidCards")
    public Object[][] invalidCards() {
        return new Object[][]{
                { new PaymentCard("Bad", "1234", "", "01", "2020") }
        };
    }
}
