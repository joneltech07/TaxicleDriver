package com.example.taxicle_driver.Model;

public class AdvanceBooking {
    public String id, pickUplocationName, dropOffLocationName, notes, time;
    public double pickUpLongitude, pickUpLatitude, dropOffLongitude, dropOffLatitude;
    public boolean isAccepted;

    public AdvanceBooking() {}

    public AdvanceBooking(String id, String pickUplocationName, String dropOffLocationName, String notes, String time, double pickUpLongitude, double pickUpLatitude, double dropOffLongitude, double dropOffLatitude, boolean isAccepted) {
        this.id = id;
        this.pickUplocationName = pickUplocationName;
        this.dropOffLocationName = dropOffLocationName;
        this.notes = notes;
        this.time = time;
        this.pickUpLongitude = pickUpLongitude;
        this.pickUpLatitude = pickUpLatitude;
        this.dropOffLongitude = dropOffLongitude;
        this.dropOffLatitude = dropOffLatitude;
        this.isAccepted = isAccepted;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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
