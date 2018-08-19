package com.example.user.trackgooglemaps;

import android.support.annotation.NonNull;

class LocationObject {
    private Double Latitude;
    private Double Longitude;
    private String userID;
    public LocationObject() {
    }

    public LocationObject(Double latitude, Double longitude,String userID) {
        Latitude = latitude;
        Longitude = longitude;
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

     @NonNull public Double getLatitude() {
        return Latitude;
    }

    public void setLatitude(Double latitude) {
        Latitude = latitude;
    }

    public Double getLongitude() {
        return Longitude;
    }

    public void setLongitude(Double longitude) {
        Longitude = longitude;
    }

    @Override
    public String toString() {
        return "Latitude:"+getLatitude()+", Longitude:"+getLongitude();
    }
}
