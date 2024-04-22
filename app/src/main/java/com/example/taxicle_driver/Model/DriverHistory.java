package com.example.taxicle_driver.Model;

public class DriverHistory {

    private String passengerId, pickupLocation, dropoffLocation;
    private String timeStamp;

    double totalFare;

    public DriverHistory() {}

    public DriverHistory(String passengerId, String pickupLocation, String dropoffLocation, String timeStamp, double totalFare) {
        this.passengerId = passengerId;
        this.pickupLocation = pickupLocation;
        this.dropoffLocation = dropoffLocation;
        this.timeStamp = timeStamp;
        this.totalFare = totalFare;
    }

    public String getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(String passengerId) {
        this.passengerId = passengerId;
    }

    public double getTotalFare() {
        return totalFare;
    }

    public void setTotalFare(double totalFare) {
        this.totalFare = totalFare;
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

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
