package com.example.taxicle_driver.constructor;

public class Passenger {

    public String name, id, locationName;
    public double longitude, latitude;

    public Passenger() {

    }
    public Passenger(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public Passenger(double longitude, double latitude, String locationName) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.locationName = locationName;
    }

    public Passenger(String name, String id, String locationName, double longitude, double latitude) {
        this.name = name;
        this.id = id;
        this.locationName = locationName;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
