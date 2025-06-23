package com.courier.model;

public class User {
    private String fullName;
    private String username;
    private String contactNumber;
    private String address;
    private String city;
    private String province;
    private String pin;

    public User() {}

    public User(String fullName, String username, String contactNumber, 
                String address, String city, String province, String pin) {
        this.fullName = fullName;
        this.username = username;
        this.contactNumber = contactNumber;
        this.address = address;
        this.city = city;
        this.province = province;
        this.pin = pin;
    }

    // Getters and Setters
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getProvince() { return province; }
    public void setProvince(String province) { this.province = province; }

    public String getPin() { return pin; }
    public void setPin(String pin) { this.pin = pin; }

    @Override
    public String toString() {
        return fullName + "," + username + "," + contactNumber + "," + 
               address + "," + city + "," + province + "," + pin;
    }

    public static User fromString(String data) {
        String[] parts = data.split(",");
        if (parts.length >= 7) {
            return new User(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], parts[6]);
        }
        return null;
    }
}
