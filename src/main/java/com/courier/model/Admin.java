package com.courier.model;

public class Admin {
    private String username;
    private String pin;

    public Admin() {}

    public Admin(String username, String pin) {
        this.username = username;
        this.pin = pin;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPin() { return pin; }
    public void setPin(String pin) { this.pin = pin; }

    @Override
    public String toString() {
        return username + "," + pin;
    }

    public static Admin fromString(String data) {
        String[] parts = data.split(",");
        if (parts.length >= 2) {
            return new Admin(parts[0], parts[1]);
        }
        return null;
    }
}
