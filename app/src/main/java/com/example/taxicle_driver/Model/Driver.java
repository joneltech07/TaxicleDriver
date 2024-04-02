package com.example.taxicle_driver.Model;

public class Driver {
    private String name, address, email, phone;
    public double longitude, latitude;

    public Driver() {}

    public Driver(String name, String address, String email, double longitude, double latitude, String phone) {
        this.name = name;
        this.address = address;
        this.email = email;
        this.longitude = longitude;
        this.latitude = latitude;
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
