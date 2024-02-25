package com.example.taxicle_driver.constructor;

import java.time.LocalDateTime;

public class DriverHistory {

    private String passengerId, pickupLocation, dropoffLocation;
    private LocalDateTime timeStamp;

    public DriverHistory() {}

    public DriverHistory(String passengerId, String pickupLocation, String dropoffLocation, LocalDateTime timeStamp) {
        this.passengerId = passengerId;
        this.pickupLocation = pickupLocation;
        this.dropoffLocation = dropoffLocation;
        this.timeStamp = timeStamp;
    }

    public String getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(String passengerId) {
        this.passengerId = passengerId;
    }

    public String getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public String getDropoffLocation() {
        return dropoffLocation;
    }

    public void setDropoffLocation(String dropoffLocation) {
        this.dropoffLocation = dropoffLocation;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }
}
