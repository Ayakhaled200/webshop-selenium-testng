package com.MyStore.data;

import com.github.javafaker.Faker;

public class UserBuilder {
    private final Faker f = new Faker();

    public User build() {
        String first = f.name().firstName();
        String last  = f.name().lastName();
        String email = (first + "." + last + "." + System.currentTimeMillis() + "@testmail.com").toLowerCase();

        return new User(
                first + " " + last, first, last,
                email, "P@ssw0rd123",
                f.company().name(),
                f.address().streetAddress(), f.address().secondaryAddress(),
                "Canada", "Ontario", "Toronto", "M5V 2T6",
                f.phoneNumber().cellPhone().replaceAll("\\D","")
        );
    }
}
