package com.example.taxicle_driver.constructor;

public class BookingPassenger {
    String id;
    public BookingPassenger() {}

    public BookingPassenger(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}