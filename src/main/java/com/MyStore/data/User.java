package com.MyStore.data;

public class User {
    private final String fullName;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String password;
    private final String company;
    private final String address1;
    private final String address2;
    private final String country;
    private final String state;
    private final String city;
    private final String zipcode;
    private final String mobile;

    public User(String fullName, String firstName, String lastName,
                String email, String password, String company,
                String address1, String address2, String country,
                String state, String city, String zipcode, String mobile) {
        this.fullName = fullName; this.firstName = firstName; this.lastName = lastName;
        this.email = email; this.password = password; this.company = company;
        this.address1 = address1; this.address2 = address2; this.country = country;
        this.state = state; this.city = city; this.zipcode = zipcode; this.mobile = mobile;
    }

    public String getFullName() { return fullName; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getCompany() { return company; }
    public String getAddress1() { return address1; }
    public String getAddress2() { return address2; }
    public String getCountry() { return country; }
    public String getState() { return state; }
    public String getCity() { return city; }
    public String getZipcode() { return zipcode; }
    public String getMobile() { return mobile; }
}
