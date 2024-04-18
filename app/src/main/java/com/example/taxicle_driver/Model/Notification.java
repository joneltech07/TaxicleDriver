package com.example.taxicle_driver.Model;

public class Notification {

    private boolean startPick, dropped;

    private double fare;

    public Notification() {}

    public Notification(boolean startPick, boolean dropped, double fare) {
        this.startPick = startPick;
        this.dropped = dropped;
        this.fare = fare;
    }

    public double getFare() {
        return fare;
    }

    public void setFare(double fare) {
        this.fare = fare;
    }

    public boolean isStartPick() {
        return startPick;
    }

    public void setStartPick(boolean startPick) {
        this.startPick = startPick;
    }

    public boolean isDropped() {
        return dropped;
    }

    public void setDropped(boolean dropped) {
        this.dropped = dropped;
    }
}
