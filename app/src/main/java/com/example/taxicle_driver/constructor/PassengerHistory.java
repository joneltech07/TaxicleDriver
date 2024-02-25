package com.example.taxicle_driver.constructor;

import java.time.LocalDateTime;

public class PassengerHistory {
    private String driverId, pickupLocation, dropoffLocation;
    private LocalDateTime timeStamp;

    public PassengerHistory() {}

    public PassengerHistory(String driverId, String pickupLocation, String dropoffLocation, LocalDateTime timeStamp) {
        this.driverId = driverId;
        this.pickupLocation = pickupLocation;
        this.dropoffLocation = dropoffLocation;
        this.timeStamp = timeStamp;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
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
