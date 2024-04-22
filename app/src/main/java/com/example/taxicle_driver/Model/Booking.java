package com.example.taxicle_driver.Model;

import java.util.Date;

public class Booking {
    public String id, pickUplocationName, dropOffLocationName, notes;
    public Date date;
    public double pickUpLongitude, pickUpLatitude, dropOffLongitude, dropOffLatitude, totalFare;
    public boolean isAccepted;

    public Booking() {}

    public Booking(String id, String pickUplocationName, String dropOffLocationName, String notes, Date date, double pickUpLongitude, double pickUpLatitude, double dropOffLongitude, double totalFare, double dropOffLatitude, boolean isAccepted) {
        this.id = id;
        this.pickUplocationName = pickUplocationName;
        this.dropOffLocationName = dropOffLocationName;
        this.notes = notes;
        this.date = date;
        this.pickUpLongitude = pickUpLongitude;
        this.pickUpLatitude = pickUpLatitude;
        this.dropOffLongitude = dropOffLongitude;
        this.dropOffLatitude = dropOffLatitude;
        this.isAccepted = isAccepted;
        this.totalFare = totalFare;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public double getTotalFare() {
        return totalFare;
    }

    public void setTotalFare(double totalFare) {
        this.totalFare = totalFare;
    }

    public boolean isAccepted() {
        return isAccepted;
    }

    public void setAccepted(boolean isAccepted) {
        this.isAccepted = isAccepted;
    }

    public String getPickUplocationName() {
        return pickUplocationName;
    }

    public void setPickUplocationName(String pickUplocationName) {
        this.pickUplocationName = pickUplocationName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getPickUpLongitude() {
        return pickUpLongitude;
    }

    public void setPickUpLongitude(double pickUpLongitude) {
        this.pickUpLongitude = pickUpLongitude;
    }

    public double getPickUpLatitude() {
        return pickUpLatitude;
    }

    public void setPickUpLatitude(double pickUpLatitude) {
        this.pickUpLatitude = pickUpLatitude;
    }

    public String getDropOffLocationName() {
        return dropOffLocationName;
    }

    public void setDropOffLocationName(String dropOffLocationName) {
        this.dropOffLocationName = dropOffLocationName;
    }

    public double getDropOffLongitude() {
        return dropOffLongitude;
    }

    public void setDropOffLongitude(double dropOffLongitude) {
        this.dropOffLongitude = dropOffLongitude;
    }

    public double getDropOffLatitude() {
        return dropOffLatitude;
    }

    public void setDropOffLatitude(double dropOffLatitude) {
        this.dropOffLatitude = dropOffLatitude;
    }
}
