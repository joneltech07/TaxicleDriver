package com.example.taxicle_driver.Model;

public class AcceptedBooking {

    private String passengerId, driverId;

    public AcceptedBooking() {}

    public AcceptedBooking(String passengerId, String driverId) {
        this.passengerId = passengerId;
        this.driverId = driverId;
    }

    public String getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(String passengerId) {
        this.passengerId = passengerId;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }
}
